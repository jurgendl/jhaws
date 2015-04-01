package org.jhaws.common.lang;

import java.util.Iterator;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexIterator implements Iterator<Matcher> {
	protected Matcher matcher;

	public Matcher matcher() {
		return matcher;
	}

	public RegexIterator(String regex, String text) {
		this(Pattern.compile(regex), text);
	}

	public RegexIterator(String regex, int flags, String text) {
		this(Pattern.compile(regex, flags), text);
	}

	public RegexIterator(Pattern pattern, String text) {
		this(pattern.matcher(text));
	}

	public RegexIterator(Matcher matcher) {
		this.matcher = matcher;
	}

	@Override
	public Matcher next() {
		return matcher;
	}

	@Override
	public boolean hasNext() {
		return matcher.find();
	}

	public <C extends Consumer<Matcher>> C stream(C consumer) {
		Collections8.stream(this).forEach(consumer);
		return consumer;
	}

	public String stream(Function<Matcher, String> converter) {
		return stream((matcher, buffer) -> matcher.appendReplacement(buffer, converter.apply(matcher)));
	}

	public String stream(BiConsumer<Matcher, StringBuffer> consumer) {
		StringBuffer buffer = new StringBuffer();
		stream((Consumer<Matcher>) matcher -> consumer.accept(matcher, buffer));
		matcher.appendTail(buffer);
		return buffer.toString();
	}
}