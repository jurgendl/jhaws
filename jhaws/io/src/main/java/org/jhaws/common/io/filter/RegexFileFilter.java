package org.jhaws.common.io.filter;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * checks if a filename succeeds or fails with a regular expression, this
 * expression can contain special strings like #-number# , where 'number' is a
 * signed integer, this string will be replaced by a signed integer with the
 * value of the lenght of the filename String minus the value of 'number'<br>
 * <br>
 * for example: [^$]{#-6#}.class<br>
 * will let filenames pass that end with .class and don't contain $ in the
 * filename and pathname<br>
 * 
 * @author Jurgen
 * @version 2.0.0 - 27 June 2006
 * 
 * @see org.jhaws.common.io.filter.AbstractFileFilter
 */
public class RegexFileFilter extends AbstractFileFilter {
	/** case (in)sensitive? */
	private Case caseSensitive;

	/** regular expression */
	private String regex;

	/**
	 * Creates a new RegexFileFilter object.
	 * 
	 * 
	 * @param regex
	 *            regular expression
	 */
	public RegexFileFilter(String regex) {
		this("regular expression filter: " + regex, regex, Case.INSENSITIVE); //$NON-NLS-1$
	}

	/**
	 * Creates a new RegexFileFilter object.
	 * 
	 * @param regex
	 *            regular expression
	 * @param caseSensitive
	 *            Case
	 */
	public RegexFileFilter(String regex, Case caseSensitive) {
		this("regular expression filter: " + regex, regex, caseSensitive); //$NON-NLS-1$
	}

	/**
	 * Creates a new RegexFileFilter object.
	 * 
	 * @param description
	 *            description
	 * @param regex
	 *            regular expression
	 */
	public RegexFileFilter(String description, String regex) {
		this(description, regex, Case.INSENSITIVE);
	}

	/**
	 * Creates a new RegexFileFilter object.
	 * 
	 * @param description
	 *            description
	 * @param regex
	 *            regular expression
	 * @param caseSensitive
	 *            Case
	 */
	public RegexFileFilter(String description, String regex, Case caseSensitive) {
		super(description);
		this.caseSensitive = caseSensitive;
		this.regex = regex;
	}

	/**
	 * 
	 * @see org.jhaws.common.io.filter.AbstractFileFilter#acceptFile(java.io.File)
	 */
	@Override
	public boolean acceptFile(File f) {
		return this.acceptFile(f.getAbsolutePath());
	}

	/**
	 * compairs file string against regular expression
	 * 
	 * @param name
	 *            NA
	 * 
	 * @return NA
	 */
	protected final boolean acceptFile(String name) {
		String nameFilter = this.replaceSpecialBlocksInRegex(this.regex, name.length());
		Pattern p;

		if (this.caseSensitive == Case.SENSITIVE) {
			p = Pattern.compile(nameFilter);
		} else {
			p = Pattern.compile(nameFilter, Pattern.CASE_INSENSITIVE);
		}

		Matcher m = p.matcher(name);

		return m.find();
	}

	/**
	 * used by function {@link #replaceSpecialBlocksInRegex(String, int)} for
	 * each number
	 * 
	 * @param _regex
	 *            : String : regex
	 * @param length
	 *            : int : length of String
	 * @param numberToReplace
	 *            : int : number
	 * 
	 * @return : String : string with replaced blocks
	 */
	private final String replaceSpecialBlockInRegex(final String _regex, final int length, final int numberToReplace) {
		Pattern p = Pattern.compile("#-" + numberToReplace + "#"); //$NON-NLS-1$ //$NON-NLS-2$
		Matcher m = p.matcher(_regex);
		int l = length - numberToReplace;

		if (l < 0) {
			l = 0;
		}

		String tmp = m.replaceAll(Integer.toString(l));

		return tmp;
	}

	/**
	 * replaces patters like <i>#-length#</i> by <i>length_of_string -
	 * length</i>
	 * 
	 * @param _regex
	 *            : String : regex
	 * @param length
	 *            : int : length of String
	 * 
	 * @return : String : string with replaced blocks
	 */
	private final String replaceSpecialBlocksInRegex(final String _regex, final int length) {
		// #-x# => length-x
		String tmp0 = _regex;
		Pattern p = Pattern.compile("#-.#"); //$NON-NLS-1$
		Matcher m = p.matcher(_regex);

		while (m.find()) {
			String tmp = m.group().substring(2, m.group().length() - 1);
			tmp0 = this.replaceSpecialBlockInRegex(tmp0, length, Integer.parseInt(tmp));
		}

		return tmp0;
	}
}
