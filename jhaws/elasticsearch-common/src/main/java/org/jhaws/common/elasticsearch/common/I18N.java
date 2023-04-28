package org.jhaws.common.elasticsearch.common;

import org.apache.commons.lang3.StringUtils;

// @JsonUnwrapped(prefix = "${FIELDNAME}_")
// @NestedField
@SuppressWarnings("serial")
public class I18N extends I18NBase {
    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.japanese)
    private String japanese;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.chinese)
    private String chinese;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.korean)
    private String korean;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.ukrainian)
    private String ukrainian;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.polish)
    private String polish;

    public I18N() {
        super();
    }

    public I18N(Language language, String value) {
        super(language, value);
    }

    public I18N(String iso, String value) {
        super(iso, value);
    }

    @Override
    public void resetValue() {
        super.resetValue();

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
        switch (language) {
            case arabic:
                arabic = value;
                break;
            case armenian:
                armenian = value;
                break;
            case basque:
                basque = value;
                break;
            case bengali:
                bengali = value;
                break;
            case bulgarian:
                bulgarian = value;
                break;
            case catalan:
                catalan = value;
                break;
            case czech:
                czech = value;
                break;
            case danish:
                danish = value;
                break;
            case dutch:
                dutch = value;
                break;
            case english:
                english = value;
                break;
            case estonian:
                estonian = value;
                break;
            case finnish:
                finnish = value;
                break;
            case french:
                french = value;
                break;
            case galician:
                galician = value;
                break;
            case german:
                german = value;
                break;
            case greek:
                greek = value;
                break;
            case hindi:
                hindi = value;
                break;
            case hungarian:
                hungarian = value;
                break;
            case indonesian:
                indonesian = value;
                break;
            case irish:
                irish = value;
                break;
            case italian:
                italian = value;
                break;
            case latvian:
                latvian = value;
                break;
            case lithuanian:
                lithuanian = value;
                break;
            case norwegian:
                norwegian = value;
                break;
            case persian:
                persian = value;
                break;
            case portuguese:
                portuguese = value;
                break;
            case romanian:
                romanian = value;
                break;
            case russian:
                russian = value;
                break;
            case spanish:
                spanish = value;
                break;
            case swedish:
                swedish = value;
                break;
            case turkish:
                turkish = value;
                break;
            case thai:
                thai = value;
                break;
            //
            case japanese:
                japanese = value;
                break;
            case chinese:
                chinese = value;
                break;
            case korean:
                korean = value;
                break;
            case ukrainian:
                ukrainian = value;
                break;
            case polish:
                polish = value;
                break;
            //
            default:
                unknown = value;
                break;
        }
    }

    @Override
    public String getValue(Language lang) {
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
    public String getValue() {
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

    public String getJapanese() {
        return japanese;
    }

    public void setJapanese(String japanese) {
        this.japanese = japanese;
    }

    public String getChinese() {
        return chinese;
    }

    public void setChinese(String chinese) {
        this.chinese = chinese;
    }

    public String getKorean() {
        return korean;
    }

    public void setKorean(String korean) {
        this.korean = korean;
    }

    public String getUkrainian() {
        return ukrainian;
    }

    public void setUkrainian(String ukrainian) {
        this.ukrainian = ukrainian;
    }

    public String getPolish() {
        return polish;
    }

    public void setPolish(String polish) {
        this.polish = polish;
    }
}
