package org.jhaws.common.lang;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @since 1.8
 */
public class RegexIterator implements Iterator<Match>, Match {
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
    public Match next() {
        return new MatchImpl(matcher);
    }

    @Override
    public boolean hasNext() {
        return matcher.find();
    }

    @Override
    public int start() {
        return matcher.start();
    }

    @Override
    public int start(int group) {
        return matcher.start(group);
    }

    @Override
    public int start(String name) {
        return matcher.start(name);
    }

    @Override
    public int end() {
        return matcher.end();
    }

    @Override
    public int end(int group) {
        return matcher.end(group);
    }

    @Override
    public int end(String name) {
        return matcher.end(name);
    }

    @Override
    public String group() {
        return matcher.group();
    }

    @Override
    public String get() {
        return matcher.group();
    }

    @Override
    public List<String> groups() {
        List<String> tmp = new ArrayList<>();
        for (int i = 1; i <= matcher.groupCount(); i++) {
            tmp.add(matcher.group(i));
        }
        return tmp;
    }

    @Override
    public String group(int group) {
        return matcher.group(group);
    }

    @Override
    public String group(String name) {
        return matcher.group(name);
    }

    @Override
    public int groupCount() {
        return matcher.groupCount();
    }

    public <C extends Consumer<Match>> C streamConsumer(C consumer) {
        matcher.reset();
        stream().forEach(consumer);
        return consumer;
    }

    @Override
    public String toString() {
        return group();
    }

    public Stream<Match> parallelStream() {
        return stream().collect(Collectors.toList()).parallelStream();
    }

    public Stream<Match> stream() {
        return CollectionUtils8.stream(this);
    }

    public List<List<String>> all() {
        List<List<String>> matches = new ArrayList<>();
        streamConsumer(match -> matches.add(match.groups()));
        return matches;
    }

    public List<String> simple() {
        List<String> matches = new ArrayList<>();
        streamConsumer(match -> matches.add(match.group()));
        return matches;
    }

    public String streamFunction(Function<Match, ?> converter) {
        return streamBiConsumer((match, buffer) -> RegexIterator.this.matcher.appendReplacement(buffer, toString(converter.apply(match))));
    }

    protected String toString(Object converted) {
        return converted == null ? "" : converted.toString();
    }

    public String streamBiConsumer(BiConsumer<Match, StringBuffer> consumer) {
        StringBuffer buffer = new StringBuffer();
        streamConsumer((Consumer<Match>) match -> consumer.accept(match, buffer));
        matcher.appendTail(buffer);
        return buffer.toString();
    }
}
