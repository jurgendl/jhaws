package org.jhaws.common.elasticsearch.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.SortedSet;
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
                if (arabic == null) arabic = new ArrayList<>();
                arabic.add(value);
                break;
            case armenian:
                if (armenian == null) armenian = new ArrayList<>();
                armenian.add(value);
                break;
            case basque:
                if (basque == null) basque = new ArrayList<>();
                basque.add(value);
                break;
            case bengali:
                if (bengali == null) bengali = new ArrayList<>();
                bengali.add(value);
                break;
            case bulgarian:
                if (bulgarian == null) bulgarian = new ArrayList<>();
                bulgarian.add(value);
                break;
            case catalan:
                if (catalan == null) catalan = new ArrayList<>();
                catalan.add(value);
                break;
            case czech:
                if (czech == null) czech = new ArrayList<>();
                czech.add(value);
                break;
            case danish:
                if (danish == null) danish = new ArrayList<>();
                danish.add(value);
                break;
            case dutch:
                if (dutch == null) dutch = new ArrayList<>();
                dutch.add(value);
                break;
            case english:
                if (english == null) english = new ArrayList<>();
                english.add(value);
                break;
            case estonian:
                if (estonian == null) estonian = new ArrayList<>();
                estonian.add(value);
                break;
            case finnish:
                if (finnish == null) finnish = new ArrayList<>();
                finnish.add(value);
                break;
            case french:
                if (french == null) french = new ArrayList<>();
                french.add(value);
                break;
            case galician:
                if (galician == null) galician = new ArrayList<>();
                galician.add(value);
                break;
            case german:
                if (german == null) german = new ArrayList<>();
                german.add(value);
                break;
            case greek:
                if (greek == null) greek = new ArrayList<>();
                greek.add(value);
                break;
            case hindi:
                if (hindi == null) hindi = new ArrayList<>();
                hindi.add(value);
                break;
            case hungarian:
                if (hungarian == null) hungarian = new ArrayList<>();
                hungarian.add(value);
                break;
            case indonesian:
                if (indonesian == null) indonesian = new ArrayList<>();
                indonesian.add(value);
                break;
            case irish:
                if (irish == null) irish = new ArrayList<>();
                irish.add(value);
                break;
            case italian:
                if (italian == null) italian = new ArrayList<>();
                italian.add(value);
                break;
            case latvian:
                if (latvian == null) latvian = new ArrayList<>();
                latvian.add(value);
                break;
            case lithuanian:
                if (lithuanian == null) lithuanian = new ArrayList<>();
                lithuanian.add(value);
                break;
            case norwegian:
                if (norwegian == null) norwegian = new ArrayList<>();
                norwegian.add(value);
                break;
            case persian:
                if (persian == null) persian = new ArrayList<>();
                persian.add(value);
                break;
            case portuguese:
                if (portuguese == null) portuguese = new ArrayList<>();
                portuguese.add(value);
                break;
            case romanian:
                if (romanian == null) romanian = new ArrayList<>();
                romanian.add(value);
                break;
            case russian:
                if (russian == null) russian = new ArrayList<>();
                russian.add(value);
                break;
            case spanish:
                if (spanish == null) spanish = new ArrayList<>();
                spanish.add(value);
                break;
            case swedish:
                if (swedish == null) swedish = new ArrayList<>();
                swedish.add(value);
                break;
            case turkish:
                if (turkish == null) turkish = new ArrayList<>();
                turkish.add(value);
                break;
            case thai:
                if (thai == null) thai = new ArrayList<>();
                thai.add(value);
                break;
            //
            case japanese:
                if (japanese == null) japanese = new ArrayList<>();
                japanese.add(value);
                break;
            case chinese:
                if (chinese == null) chinese = new ArrayList<>();
                chinese.add(value);
                break;
            case korean:
                if (korean == null) korean = new ArrayList<>();
                korean.add(value);
                break;
            case ukrainian:
                if (ukrainian == null) ukrainian = new ArrayList<>();
                ukrainian.add(value);
                break;
            case polish:
                if (polish == null) polish = new ArrayList<>();
                polish.add(value);
                break;
            //
            default:
                if (unknown == null) unknown = new ArrayList<>();
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

    @Override
    public List<String> getValue() {
        if (japanese != null) {
            return japanese;
        }
        if (chinese != null) {
            return chinese;
        }
        if (korean != null) {
            return korean;
        }
        if (ukrainian != null) {
            return ukrainian;
        }
        if (polish != null) {
            return polish;
        }
        //
        return super.getValue();
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
}
