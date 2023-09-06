package org.jhaws.common.elasticsearch.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

// @JsonUnwrapped(prefix = "${FIELDNAME}_")
// @NestedField
@SuppressWarnings("serial")
public class I18NS extends I18NSBase {
    // public static void main(String[] args) {
    // if (false) {
    // String t = "case \"${ISO}\": if (${L} == null) ${L} = new ArrayList<>(); ${L}.add(value); break;";
    // Arrays.stream(Language.values()).filter(a -> a.iso() != null).forEach(a -> {
    // System.out.println(t.replace("${L}", a.name()).replace("${ISO}", a.iso().toUpperCase()));
    // });
    // }
    // if (false) {
    // String t = " @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.${L})\n"//
    // + " protected List<String> ${L};";//
    // Arrays.stream(Language.values()).filter(a -> a.iso() != null).forEach(a -> {
    // System.out.println(t.replace("${L}", a.name()).replace("${ISO}", a.iso().toUpperCase()));
    // });
    // }
    // if (false) {
    // String t = "if(${L}!=null) { return ${L}; }";//
    // Arrays.stream(Language.values()).filter(a -> a.iso() != null).forEach(a -> {
    // System.out.println(t.replace("${L}", a.name()).replace("${ISO}", a.iso().toUpperCase()));
    // });
    // }
    // if (false) {
    // String t = "case ${L}: if(${L}==null) ${L}=new ArrayList<>(); ${L}.add(value); break;";
    // Arrays.stream(Language.values()).filter(a -> a.iso() != null).forEach(a -> {
    // System.out.println(t.replace("${L}", a.name()).replace("${ISO}", a.iso().toUpperCase()));
    // });
    // }
    // if (false) {
    // String t = "${L}=null;";
    // Arrays.stream(Language.values()).filter(a -> a.iso() != null).forEach(a -> {
    // System.out.println(t.replace("${L}", a.name()).replace("${ISO}", a.iso().toUpperCase()));
    // });
    // }
    // if (false) {
    // String t = "if (${L} != null && ${L}.size() > 1) ${L} = ${L}.stream().distinct().collect(Collectors.toList());";
    // Arrays.stream(Language.values()).filter(a -> a.iso() != null).forEach(a -> {
    // System.out.println(t.replace("${L}", a.name()).replace("${ISO}", a.iso().toUpperCase()));
    // });
    // }
    // if (true) {
    // String t = "if (${L} != null) set.addAll(${L});";
    // Arrays.stream(Language.values()).filter(a -> a.iso() != null).forEach(a -> {
    // System.out.println(t.replace("${L}", a.name()).replace("${ISO}", a.iso().toUpperCase()));
    // });
    // }
    // }

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.japanese)
    protected List<String> japanese;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.chinese)
    protected List<String> chinese;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.korean)
    protected List<String> korean;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.ukrainian)
    protected List<String> ukrainian;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.polish)
    protected List<String> polish;

    public I18NS() {
        super();
    }

    public I18NS(Language language, Collection<String> values0) {
        super(language, values0);
    }

    public I18NS(Language language, String... values0) {
        super(language, values0);
    }

    public I18NS(String iso, String... values0) {
        super(iso, values0);
    }

    @Override
    public SortedSet<String> collect() {
        SortedSet<String> set = super.collect();

        if (japanese != null) set.addAll(japanese);
        if (chinese != null) set.addAll(chinese);
        if (korean != null) set.addAll(korean);
        if (ukrainian != null) set.addAll(ukrainian);
        if (polish != null) set.addAll(polish);

        return set;
    }

    @Override
    public void distinct() {
        super.distinct();

        if (japanese != null && japanese.size() > 1) japanese = japanese.stream().distinct().collect(Collectors.toList());
        if (chinese != null && chinese.size() > 1) chinese = chinese.stream().distinct().collect(Collectors.toList());
        if (korean != null && korean.size() > 1) korean = korean.stream().distinct().collect(Collectors.toList());
        if (ukrainian != null && ukrainian.size() > 1) ukrainian = ukrainian.stream().distinct().collect(Collectors.toList());
        if (polish != null && polish.size() > 1) polish = polish.stream().distinct().collect(Collectors.toList());
    }

    @Override
    public void resetValues() {
        super.resetValues();

        japanese = null;
        chinese = null;
        korean = null;
        ukrainian = null;
        polish = null;
    }

    @Override
    protected void value(Language language, String value) {
        if (StringUtils.isBlank(value)) return;
        if (!this.values.contains(value)) this.values.add(value);
        if (language == null) language = Language.uninitialized;
        switch (language) {
            case arabic:
                if (arabic == null)
                    arabic = new ArrayList<>();
                else if (arabic.contains(value))
                    break;
                else
                    arabic.add(value);
                break;

            case armenian:
                if (armenian == null)
                    armenian = new ArrayList<>();
                else if (armenian.contains(value))
                    break;
                else
                    armenian.add(value);
                break;

            case basque:
                if (basque == null)
                    basque = new ArrayList<>();
                else if (basque.contains(value))
                    break;
                else
                    basque.add(value);
                break;

            case bengali:
                if (bengali == null)
                    bengali = new ArrayList<>();
                else if (bengali.contains(value))
                    break;
                else
                    bengali.add(value);
                break;

            case bulgarian:
                if (bulgarian == null)
                    bulgarian = new ArrayList<>();
                else if (bulgarian.contains(value))
                    break;
                else
                    bulgarian.add(value);
                break;

            case catalan:
                if (catalan == null)
                    catalan = new ArrayList<>();
                else if (catalan.contains(value))
                    break;
                else
                    catalan.add(value);
                break;

            case czech:
                if (czech == null)
                    czech = new ArrayList<>();
                else if (czech.contains(value))
                    break;
                else
                    czech.add(value);
                break;

            case danish:
                if (danish == null)
                    danish = new ArrayList<>();
                else if (danish.contains(value))
                    break;
                else
                    danish.add(value);
                break;

            case dutch:
                if (dutch == null)
                    dutch = new ArrayList<>();
                else if (dutch.contains(value))
                    break;
                else
                    dutch.add(value);
                break;

            case english:
                if (english == null)
                    english = new ArrayList<>();
                else if (english.contains(value))
                    break;
                else
                    english.add(value);
                break;

            case estonian:
                if (estonian == null)
                    estonian = new ArrayList<>();
                else if (estonian.contains(value))
                    break;
                else
                    estonian.add(value);
                break;

            case finnish:
                if (finnish == null)
                    finnish = new ArrayList<>();
                else if (finnish.contains(value))
                    break;
                else
                    finnish.add(value);
                break;

            case french:
                if (french == null)
                    french = new ArrayList<>();
                else if (french.contains(value))
                    break;
                else
                    french.add(value);
                break;

            case galician:
                if (galician == null)
                    galician = new ArrayList<>();
                else if (galician.contains(value))
                    break;
                else
                    galician.add(value);
                break;

            case german:
                if (german == null)
                    german = new ArrayList<>();
                else if (german.contains(value))
                    break;
                else
                    german.add(value);
                break;

            case greek:
                if (greek == null)
                    greek = new ArrayList<>();
                else if (greek.contains(value))
                    break;
                else
                    greek.add(value);
                break;

            case hindi:
                if (hindi == null)
                    hindi = new ArrayList<>();
                else if (hindi.contains(value))
                    break;
                else
                    hindi.add(value);
                break;

            case hungarian:
                if (hungarian == null)
                    hungarian = new ArrayList<>();
                else if (hungarian.contains(value))
                    break;
                else
                    hungarian.add(value);
                break;

            case indonesian:
                if (indonesian == null)
                    indonesian = new ArrayList<>();
                else if (indonesian.contains(value))
                    break;
                else
                    indonesian.add(value);
                break;

            case irish:
                if (irish == null)
                    irish = new ArrayList<>();
                else if (irish.contains(value))
                    break;
                else
                    irish.add(value);
                break;

            case italian:
                if (italian == null)
                    italian = new ArrayList<>();
                else if (italian.contains(value))
                    break;
                else
                    italian.add(value);
                break;

            case latvian:
                if (latvian == null)
                    latvian = new ArrayList<>();
                else if (latvian.contains(value))
                    break;
                else
                    latvian.add(value);
                break;

            case lithuanian:
                if (lithuanian == null)
                    lithuanian = new ArrayList<>();
                else if (lithuanian.contains(value))
                    break;
                else
                    lithuanian.add(value);
                break;

            case norwegian:
                if (norwegian == null)
                    norwegian = new ArrayList<>();
                else if (norwegian.contains(value))
                    break;
                else
                    norwegian.add(value);
                break;

            case persian:
                if (persian == null)
                    persian = new ArrayList<>();
                else if (persian.contains(value))
                    break;
                else
                    persian.add(value);
                break;

            case portuguese:
                if (portuguese == null)
                    portuguese = new ArrayList<>();
                else if (portuguese.contains(value))
                    break;
                else
                    portuguese.add(value);
                break;

            case romanian:
                if (romanian == null)
                    romanian = new ArrayList<>();
                else if (romanian.contains(value))
                    break;
                else
                    romanian.add(value);
                break;

            case russian:
                if (russian == null)
                    russian = new ArrayList<>();
                else if (russian.contains(value))
                    break;
                else
                    russian.add(value);
                break;

            case spanish:
                if (spanish == null)
                    spanish = new ArrayList<>();
                else if (spanish.contains(value))
                    break;
                else
                    spanish.add(value);
                break;

            case swedish:
                if (swedish == null)
                    swedish = new ArrayList<>();
                else if (swedish.contains(value))
                    break;
                else
                    swedish.add(value);
                break;

            case turkish:
                if (turkish == null)
                    turkish = new ArrayList<>();
                else if (turkish.contains(value))
                    break;
                else
                    turkish.add(value);
                break;

            case thai:
                if (thai == null)
                    thai = new ArrayList<>();
                else if (thai.contains(value))
                    break;
                else
                    thai.add(value);
                break;

            case japanese:
                if (japanese == null)
                    japanese = new ArrayList<>();
                else if (japanese.contains(value))
                    break;
                else
                    japanese.add(value);
                break;

            case chinese:
                if (chinese == null)
                    chinese = new ArrayList<>();
                else if (chinese.contains(value))
                    break;
                else
                    chinese.add(value);
                break;

            case korean:
                if (korean == null)
                    korean = new ArrayList<>();
                else if (korean.contains(value))
                    break;
                else
                    korean.add(value);
                break;

            case ukrainian:
                if (ukrainian == null)
                    ukrainian = new ArrayList<>();
                else if (ukrainian.contains(value))
                    break;
                else
                    ukrainian.add(value);
                break;

            case polish:
                if (polish == null)
                    polish = new ArrayList<>();
                else if (polish.contains(value))
                    break;
                else
                    polish.add(value);
                break;
            //
            default:
                if (unknown == null)
                    unknown = new ArrayList<>();
                else if (unknown.contains(value))
                    break;
                else
                    unknown.add(value);
                break;
        }
    }

    @Override
    public List<String> getValue(Language lang) {
        if (Language.japanese == lang) {
            return japanese;
        }
        if (Language.chinese == lang) {
            return chinese;
        }
        if (Language.korean == lang) {
            return korean;
        }
        if (Language.ukrainian == lang) {
            return ukrainian;
        }
        if (Language.polish == lang) {
            return polish;
        }

        return super.getValue(lang);
    }

    public List<String> getJapanese() {
        return japanese;
    }

    public void setJapanese(List<String> japanese) {
        this.japanese = japanese;
    }

    public List<String> getChinese() {
        return chinese;
    }

    public void setChinese(List<String> chinese) {
        this.chinese = chinese;
    }

    public List<String> getKorean() {
        return korean;
    }

    public void setKorean(List<String> korean) {
        this.korean = korean;
    }

    public List<String> getUkrainian() {
        return ukrainian;
    }

    public void setUkrainian(List<String> ukrainian) {
        this.ukrainian = ukrainian;
    }

    public List<String> getPolish() {
        return polish;
    }

    public void setPolish(List<String> polish) {
        this.polish = polish;
    }

    @Override
    public String toString() {
        return collect().toString();
    }

    @Override
    public void operate(UnaryOperator<List<String>> i) {
        if (japanese != null) japanese = i.apply(japanese);
        if (chinese != null) chinese = i.apply(chinese);
        if (korean != null) korean = i.apply(korean);
        if (ukrainian != null) ukrainian = i.apply(ukrainian);
        if (polish != null) polish = i.apply(polish);
        //
        super.operate(i);
    }

    @Override
    public Map<Language, List<String>> toMap() {
        Map<Language, List<String>> all = super.toMap();
        //
        if (japanese != null && !japanese.isEmpty()) all.put(Language.japanese, japanese);
        if (chinese != null && !chinese.isEmpty()) all.put(Language.chinese, chinese);
        if (korean != null && !korean.isEmpty()) all.put(Language.korean, korean);
        if (ukrainian != null && !ukrainian.isEmpty()) all.put(Language.ukrainian, ukrainian);
        if (polish != null && !polish.isEmpty()) all.put(Language.polish, polish);
        //
        return all;
    }
}
