package org.jhaws.common.web.servlets;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A file servlet supporting resume of downloads and client-side caching and
 * GZIP of text content. This servlet can also be used for images, client-side
 * caching would become more efficient. This servlet can also be used for text
 * files, GZIP would decrease network bandwidth.
 *
 * @author BalusC
 * @link http://balusc.blogspot.com/2009/02/fileservlet-supporting-resume-and.html
 */
@SuppressWarnings("serial")
public abstract class StreamingServlet extends HttpServlet {
	/**
	 * This class represents a byte range.
	 */
	protected class Range {
		long start;

		long end;

		long length;

		long total;

		/**
		 * Construct a byte range.
		 *
		 * @param start Start of the byte range.
		 * @param end   End of the byte range.
		 * @param total Total length of the byte source.
		 */
		public Range(long start, long end, long total) {
			this.start = start;
			this.end = end;
			length = end - start + 1;
			this.total = total;
		}

	}

	protected static final int DEFAULT_BUFFER_SIZE = 2 << 12; // ..bytes = 8KB.

	// ..ms = 1 week.
	protected static final long DEFAULT_EXPIRE_TIME = 60l * 60l * 24l * 7l;

	protected static final String MULTIPART_BOUNDARY = "MULTIPART_BYTERANGES";

	public static String decode(String name) {
		try {
			return URLDecoder.decode(name, StandardCharsets.UTF_8.toString());
		} catch (UnsupportedEncodingException ex) {
			throw new IllegalArgumentException(ex);
		}
	}

	public static String encode(String name) {
		try {
			return URLEncoder.encode(name, StandardCharsets.UTF_8.toString());
		} catch (UnsupportedEncodingException ex) {
			throw new IllegalArgumentException(ex);
		}
	}

	/**
	 * Returns true if the given accept header accepts the given value.
	 *
	 * @param acceptHeader The accept header.
	 * @param toAccept     The value to be accepted.
	 * @return True if the given accept header accepts the given value.
	 */
	protected boolean accepts(String acceptHeader, String toAccept) {
		String[] acceptValues = acceptHeader.split("\\s*(,|;)\\s*");
		Arrays.sort(acceptValues);
		return Arrays.binarySearch(acceptValues, toAccept) > -1
				|| Arrays.binarySearch(acceptValues, toAccept.replaceAll("/.*$", "/*")) > -1
				|| Arrays.binarySearch(acceptValues, "*/*") > -1;
	}

	/**
	 * Close the given resource.
	 *
	 * @param resource The resource to be closed.
	 */
	protected void close(Closeable resource) {
		if (resource != null) {
			try {
				resource.close();
			} catch (IOException ignore) {
				// Ignore IOException. If you want to handle this anyway, it
				// might be useful to know
				// that this will generally only be thrown when the client
				// aborted the request.
			}
		}
	}

	/**
	 * Copy the given byte range of the given input to the given output.
	 *
	 * @param input  The input to copy the given range to the given output for.
	 * @param output The output to copy the given range from the given input for.
	 * @param start  Start of the byte range.
	 * @param length Length of the byte range.
	 * @throws IOException If something fails at I/O level.
	 */
	protected void copy(SeekableByteChannel input, OutputStream output, long start, long length) throws IOException {
		// http://andreinc.net/2013/12/09/java-7-nio-2-how-to-use-seekablebytechannel-interface-for-random-access-to-files-raf/
		// System.out.println(start + "/" + length);
		ByteBuffer buffer = ByteBuffer.allocate(getBufferSize());
		int read;
		if (input.size() == length) {
			input.position(0l);
			// Write full range.
			while ((read = input.read(buffer)) > 0) {
				// System.out.println(read);
				output.write(buffer.array(), 0, read);
				buffer.clear();
			}
		} else {
			// Write partial range.
			input.position(start);
			long toRead = length;

			while ((read = input.read(buffer)) > 0) {
				// System.out.println(read);
				if ((toRead -= read) > 0) {
					output.write(buffer.array(), 0, read);
					buffer.clear();
				} else {
					output.write(buffer.array(), 0, (int) toRead + read);
					buffer.clear();
					break;
				}
			}
		}
	}

	/**
	 * Process GET request.
	 *
	 * @see HttpServlet#doGet(HttpServletRequest, HttpServletResponse).
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Process request with content.
		processRequest(request, response, true);
	}

	/**
	 * Process HEAD request. This returns the same headers as GET request, but
	 * without content.
	 *
	 * @see HttpServlet#doHead(HttpServletRequest, HttpServletResponse).
	 */
	@Override
	protected void doHead(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Process request without content.
		processRequest(request, response, false);
	}

	protected int getBufferSize() {
		return DEFAULT_BUFFER_SIZE;
	}

	protected long getExpireTime() {
		return DEFAULT_EXPIRE_TIME;
	}

	protected abstract StreamingSource getStream(String requestedFile) throws UnsupportedEncodingException, IOException;

	/**
	 * Initialize the servlet.
	 *
	 * @see HttpServlet#init().
	 */
	@Override
	public void init() throws ServletException {
		//
	}

	/**
	 * Returns true if the given match header matches the given value.
	 *
	 * @param matchHeader The match header.
	 * @param toMatch     The value to be matched.
	 * @return True if the given match header matches the given value.
	 */
	protected boolean matches(String matchHeader, String toMatch) {
		String[] matchValues = matchHeader.split("\\s*,\\s*");
		Arrays.sort(matchValues);
		return Arrays.binarySearch(matchValues, toMatch) > -1 || Arrays.binarySearch(matchValues, "*") > -1;
	}

	/**
	 * Process the actual request.
	 *
	 * @param request  The request to be processed.
	 * @param response The response to be created.
	 * @param content  Whether the request body should be written (GET) or not
	 *                 (HEAD).
	 * @throws IOException If something fails at I/O level.
	 */
	protected void processRequest(HttpServletRequest request, HttpServletResponse response, boolean content)
			throws ServletException, IOException {
		// Get requested file by path info.
		String requestedFile = request.getPathInfo();
		requestedFile = request.getRequestURI().replaceFirst(request.getContextPath(), "").replaceFirst("/stream", "");

		// Check if file is actually supplied to the request URL.
		if (requestedFile == null) {
			// Do your thing if the file is not supplied to the request URL.
			// Throw an exception, or send 404, or show default/warning page, or
			// just ignore it.
			response.sendError(HttpServletResponse.SC_NOT_FOUND, requestedFile);
			return;
		}

		// url decodede filename (can contain '/')
		String relativeFileName = decode(requestedFile);
		if (relativeFileName.startsWith("/")) {
			relativeFileName = relativeFileName.substring(1);
		}
		// filename without relative path
		String shortFileName = relativeFileName.contains("/")
				? relativeFileName.substring(relativeFileName.lastIndexOf('/') + 1)
				: relativeFileName;

		// URL-decode the file name (might contain spaces and on) and prepare
		// file object.
		StreamingSource file;
		try {
			file = getStream(relativeFileName);
		} catch (IOException ex) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND, relativeFileName);
			return;
		}

		// Check if file actually exists in filesystem.
		if (!file.exists()) {
			// Do your thing if the file appears to be non-existing.
			// Throw an exception, or send 404, or show default/warning page, or
			// just ignore it.
			response.sendError(HttpServletResponse.SC_NOT_FOUND, relativeFileName);
			return;
		}

		// Prepare some variables. The ETag is an unique identifier of the file.
		long length = file.length();
		long lastModified = file.lastModified();
		String eTag = requestedFile.replace('/', '$').replace(',', '$') + "_" + length + "_" + lastModified;
		long expires = System.currentTimeMillis() + getExpireTime();

		// Validate request headers for caching
		// ---------------------------------------------------

		// If-None-Match header should contain "*" or ETag. If so, then return
		// 304.
		String ifNoneMatch = request.getHeader("If-None-Match");
		if (ifNoneMatch != null && matches(ifNoneMatch, eTag)) {
			response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
			response.setHeader("ETag", eTag); // Required in 304.
			// Postpone cache with 1 week.
			response.setDateHeader("Expires", expires);
			return;
		}

		// If-Modified-Since header should be greater than LastModified. If so,
		// then return 304.
		// This header is ignored if any If-None-Match header is specified.
		long ifModifiedSince = request.getDateHeader("If-Modified-Since");
		if (ifNoneMatch == null && ifModifiedSince != -1 && ifModifiedSince + 1000 > lastModified) {
			response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
			response.setHeader("ETag", eTag); // Required in 304.
			// Postpone cache with 1 week.
			response.setDateHeader("Expires", expires);
			return;
		}

		// Validate request headers for resume
		// ----------------------------------------------------

		// If-Match header should contain "*" or ETag. If not, then return 412.
		String ifMatch = request.getHeader("If-Match");
		if (ifMatch != null && !matches(ifMatch, eTag)) {
			response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED);
			return;
		}

		// If-Unmodified-Since header should be greater than LastModified. If
		// not, then return 412.
		long ifUnmodifiedSince = request.getDateHeader("If-Unmodified-Since");
		if (ifUnmodifiedSince != -1 && ifUnmodifiedSince + 1000 <= lastModified) {
			response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED);
			return;
		}

		// Validate and process range
		// -------------------------------------------------------------

		// Prepare some variables. The full Range represents the complete file.
		Range full = new Range(0, length - 1, length);
		List<Range> ranges = new ArrayList<>();

		// Validate and process Range and If-Range headers.
		String range = request.getHeader("Range");
		if (range != null) {

			// Range header should match format "bytes=n-n,n-n,n-n...". If not,
			// then return 416.
			if (!range.matches("^bytes=\\d*-\\d*(,\\d*-\\d*)*$")) {
				// Required in 416.
				response.setHeader("Content-Range", "bytes */" + length);
				response.sendError(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
				return;
			}

			// If-Range header should either match ETag or be greater then
			// LastModified. If not,
			// then return full file.
			String ifRange = request.getHeader("If-Range");
			if (ifRange != null && !ifRange.equals(eTag)) {
				try {
					// Throws IAE if invalid.
					long ifRangeTime = request.getDateHeader("If-Range");
					if (ifRangeTime != -1 && ifRangeTime + 1000 < lastModified) {
						ranges.add(full);
					}
				} catch (IllegalArgumentException ignore) {
					ranges.add(full);
				}
			}

			// If any valid If-Range header, then process each part of byte
			// range.
			if (ranges.isEmpty()) {
				for (String part : range.substring(6).split(",")) {
					// Assuming a file with length of 100, the following
					// examples returns bytes at:
					// 50-80 (50 to 80), 40- (40 to length=100), -20
					// (length-20=80 to length=100).
					long start = sublong(part, 0, part.indexOf("-"));
					long end = sublong(part, part.indexOf("-") + 1, part.length());

					if (start == -1) {
						start = length - end;
						end = length - 1;
					} else if (end == -1 || end > length - 1) {
						end = length - 1;
					}

					// Check if Range is syntactically valid. If not, then
					// return 416.
					if (start > end) {
						// Required in 416.
						response.setHeader("Content-Range", "bytes */" + length);
						response.sendError(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
						return;
					}

					// Add range.
					ranges.add(new Range(start, end, length));
				}
			}
		}

		// Prepare and initialize response
		// --------------------------------------------------------

		// Get content type by file name and set default GZIP support and
		// content disposition.
		String contentType = getServletContext().getMimeType(shortFileName);
		boolean acceptsGzip = false;
		String disposition = "inline";

		// If content type is unknown, then set the default value.
		// For all content types, see:
		// http://www.w3schools.com/media/media_mimeref.asp
		// To add new content types, add new mime-mapping entry in web.xml.
		if (contentType == null) {
			contentType = "application/octet-stream";
		}

		// If content type is text, then determine whether GZIP content encoding
		// is supported by
		// the browser and expand content type with the one and right character
		// encoding.
		if (contentType.startsWith("text")) {
			String acceptEncoding = request.getHeader("Accept-Encoding");
			acceptsGzip = acceptEncoding != null && accepts(acceptEncoding, "gzip");
			contentType += ";charset=" + "UTF-8";
		}

		// Else, expect for images, determine content disposition. If content
		// type is supported by
		// the browser, then set to inline, else attachment which will pop a
		// 'save as' dialogue.
		else if (!contentType.startsWith("image")) {
			String accept = request.getHeader("Accept");
			disposition = accept != null && accepts(accept, contentType) ? "inline" : "attachment";
		}

		// Initialize response.
		response.reset();
		response.setBufferSize(getBufferSize());
		response.setHeader("Content-Disposition",
				disposition + ";filename=\"" + URLEncoder.encode(shortFileName, "utf-8") + "\"");
		response.setHeader("Accept-Ranges", "bytes");
		response.setHeader("ETag", eTag);
		response.setDateHeader("Last-Modified", lastModified);
		response.setDateHeader("Expires", expires);

		// Send requested file (part(s)) to client
		// ------------------------------------------------

		// Prepare streams.
		SeekableByteChannel input = null;
		OutputStream output = null;

		try {
			// Open streams.
			input = file.newSeekableByteChannel();
			output = response.getOutputStream();

			if (ranges.isEmpty() || ranges.get(0) == full) {
				// Return full file.
				Range r = full;
				response.setContentType(contentType);
				response.setHeader("Content-Range", "bytes " + r.start + "-" + r.end + "/" + r.total);

				if (content) {
					if (acceptsGzip) {
						// The browser accepts GZIP, so GZIP the content.
						response.setHeader("Content-Encoding", "gzip");
						output = new GZIPOutputStream(output, getBufferSize());
					} else {
						// Content length is not directly predictable in case of
						// GZIP.
						// So only add it if there is no means of GZIP, else
						// browser will hang.
						response.setHeader("Content-Length", String.valueOf(r.length));
					}

					// Copy full range.
					// System.out.println("A " + ((r.end - r.start) + "//" +
					// r.length));
					copy(input, output, r.start, r.length);
				}

			} else if (ranges.size() == 1) {

				// Return single part of file.
				Range r = ranges.get(0);
				response.setContentType(contentType);
				response.setHeader("Content-Range", "bytes " + r.start + "-" + r.end + "/" + r.total);
				response.setHeader("Content-Length", String.valueOf(r.length));
				response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT); // 206.

				if (content) {
					// Copy single part range.
					// System.out.println("B " + ((r.end - r.start) + "//" +
					// r.length));
					copy(input, output, r.start, r.length);
				}

			} else {

				// Return multiple parts of file.
				response.setContentType("multipart/byteranges; boundary=" + StreamingServlet.MULTIPART_BOUNDARY);
				response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT); // 206.

				if (content) {
					// Cast back to ServletOutputStream to get the easy println
					// methods.
					ServletOutputStream sos = (ServletOutputStream) output;

					// Copy multi part range.
					for (Range r : ranges) {
						// Add multipart boundary and header fields for every
						// range.
						sos.println();
						sos.println("--" + StreamingServlet.MULTIPART_BOUNDARY);
						sos.println("Content-Type: " + contentType);
						sos.println("Content-Range: bytes " + r.start + "-" + r.end + "/" + r.total);

						// Copy single part range of multi part range.
						// System.out.println("C " + ((r.end - r.start) + "//" +
						// r.length));
						copy(input, output, r.start, r.length);
					}

					// End with multipart boundary.
					sos.println();
					sos.println("--" + StreamingServlet.MULTIPART_BOUNDARY + "--");
				}
			}
		} finally {
			// Gently close streams.
			close(output);
			close(input);
		}
	}

	/**
	 * Returns a substring of the given string value from the given begin index to
	 * the given end index as a long. If the substring is empty, then -1 will be
	 * returned
	 *
	 * @param value      The string value to return a substring as long for.
	 * @param beginIndex The begin index of the substring to be returned as long.
	 * @param endIndex   The end index of the substring to be returned as long.
	 * @return A substring of the given string value as long or -1 if substring is
	 *         empty.
	 */
	protected long sublong(String value, int beginIndex, int endIndex) {
		String substring = value.substring(beginIndex, endIndex);
		return substring.length() > 0 ? Long.parseLong(substring) : -1;
	}
}