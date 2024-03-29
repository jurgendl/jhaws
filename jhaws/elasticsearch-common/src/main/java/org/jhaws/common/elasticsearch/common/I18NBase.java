package org.jhaws.common.elasticsearch.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

// @JsonUnwrapped(prefix = "${FIELDNAME}_")
// @NestedField
@SuppressWarnings("serial")
public class I18NBase implements Serializable {
    public static final String UNKNOWN = "unknown";

    public static final String VALUES = "values";

    public static final String SUFFIX_SORTABLE_EXTENDED = ".sortableExtended";

    @Field(type = FieldType.TEXT, fielddata = Bool.TRUE, customAnalyzer = Analyzers.CUSTOM_SORTABLE_ONLY_ALPHANUMERIC_ANALYZER)
    @FieldExtra(value = { @Field(name = "sortableExtended", type = FieldType.TEXT, fielddata = Bool.TRUE, customAnalyzer = Analyzers.CUSTOM_SORTABLE_EXTENDED_ALPHANUMERIC_ANALYZER) })
    protected List<String> values = new ArrayList<>();

    @Field(type = FieldType.TEXT, customAnalyzer = Analyzers.CUSTOM_ANY_LANGUAGE_ANALYZER)
    protected String unknown;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.arabic)
    protected String arabic;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.armenian)
    protected String armenian;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.basque)
    protected String basque;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.bengali)
    protected String bengali;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.bulgarian)
    protected String bulgarian;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.catalan)
    protected String catalan;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.czech)
    protected String czech;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.danish)
    protected String danish;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.dutch)
    protected String dutch;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.english)
    protected String english;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.estonian)
    protected String estonian;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.finnish)
    protected String finnish;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.french)
    protected String french;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.galician)
    protected String galician;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.german)
    protected String german;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.greek)
    protected String greek;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.hindi)
    protected String hindi;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.hungarian)
    protected String hungarian;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.indonesian)
    protected String indonesian;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.irish)
    protected String irish;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.italian)
    protected String italian;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.latvian)
    protected String latvian;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.lithuanian)
    protected String lithuanian;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.norwegian)
    protected String norwegian;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.persian)
    protected String persian;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.portuguese)
    protected String portuguese;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.romanian)
    protected String romanian;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.russian)
    protected String russian;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.spanish)
    protected String spanish;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.swedish)
    protected String swedish;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.turkish)
    protected String turkish;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.thai)
    protected String thai;

    public I18NBase() {
        super();
    }

    public I18NBase(String iso, String value) {
        setValue(iso, value);
    }

    public I18NBase(Language language, String value) {
        setValue(language, value);
    }

    @Override
    public String toString() {
        return String.valueOf(getValue());
    }

    public void resetValue() {
        unknown = null;
        values = null;
        {
            arabic = null;
            armenian = null;
            basque = null;
            bengali = null;
            bulgarian = null;
            catalan = null;
            czech = null;
            danish = null;
            dutch = null;
            english = null;
            estonian = null;
            finnish = null;
            french = null;
            galician = null;
            german = null;
            greek = null;
            hindi = null;
            hungarian = null;
            indonesian = null;
            irish = null;
            italian = null;
            latvian = null;
            lithuanian = null;
            norwegian = null;
            persian = null;
            portuguese = null;
            romanian = null;
            russian = null;
            spanish = null;
            swedish = null;
            turkish = null;
            thai = null;
        }
    }

    public void setValue(String languageIso, String value) {
        value(Language.forIso(languageIso).orElse(null), value);
    }

    public void setValue(Language language, String value) {
        value(language, value);
    }

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
            default:
                unknown = value;
                break;
        }
    }

    public String getValue(Language lang) {
        if (Language.arabic == lang) {
            return arabic;
        }
        if (Language.armenian == lang) {
            return armenian;
        }
        if (Language.basque == lang) {
            return basque;
        }
        if (Language.bengali == lang) {
            return bengali;
        }
        if (Language.bulgarian == lang) {
            return bulgarian;
        }
        if (Language.catalan == lang) {
            return catalan;
        }
        if (Language.czech == lang) {
            return czech;
        }
        if (Language.danish == lang) {
            return danish;
        }
        if (Language.dutch == lang) {
            return dutch;
        }
        if (Language.english == lang) {
            return english;
        }
        if (Language.estonian == lang) {
            return estonian;
        }
        if (Language.finnish == lang) {
            return finnish;
        }
        if (Language.french == lang) {
            return french;
        }
        if (Language.galician == lang) {
            return galician;
        }
        if (Language.german == lang) {
            return german;
        }
        if (Language.greek == lang) {
            return greek;
        }
        if (Language.hindi == lang) {
            return hindi;
        }
        if (Language.hungarian == lang) {
            return hungarian;
        }
        if (Language.indonesian == lang) {
            return indonesian;
        }
        if (Language.irish == lang) {
            return irish;
        }
        if (Language.italian == lang) {
            return italian;
        }
        if (Language.latvian == lang) {
            return latvian;
        }
        if (Language.lithuanian == lang) {
            return lithuanian;
        }
        if (Language.norwegian == lang) {
            return norwegian;
        }
        if (Language.persian == lang) {
            return persian;
        }
        if (Language.portuguese == lang) {
            return portuguese;
        }
        if (Language.romanian == lang) {
            return romanian;
        }
        if (Language.russian == lang) {
            return russian;
        }
        if (Language.spanish == lang) {
            return spanish;
        }
        if (Language.swedish == lang) {
            return swedish;
        }
        if (Language.turkish == lang) {
            return turkish;
        }
        if (Language.thai == lang) {
            return thai;
        }
        // ------------------
        return this.unknown;
    }

    public String getValue() {
        if (arabic != null) {
            return arabic;
        }
        if (armenian != null) {
            return armenian;
        }
        if (basque != null) {
            return basque;
        }
        if (bengali != null) {
            return bengali;
        }
        if (bulgarian != null) {
            return bulgarian;
        }
        if (catalan != null) {
            return catalan;
        }
        if (czech != null) {
            return czech;
        }
        if (danish != null) {
            return danish;
        }
        if (dutch != null) {
            return dutch;
        }
        if (english != null) {
            return english;
        }
        if (estonian != null) {
            return estonian;
        }
        if (finnish != null) {
            return finnish;
        }
        if (french != null) {
            return french;
        }
        if (galician != null) {
            return galician;
        }
        if (german != null) {
            return german;
        }
        if (greek != null) {
            return greek;
        }
        if (hindi != null) {
            return hindi;
        }
        if (hungarian != null) {
            return hungarian;
        }
        if (indonesian != null) {
            return indonesian;
        }
        if (irish != null) {
            return irish;
        }
        if (italian != null) {
            return italian;
        }
        if (latvian != null) {
            return latvian;
        }
        if (lithuanian != null) {
            return lithuanian;
        }
        if (norwegian != null) {
            return norwegian;
        }
        if (persian != null) {
            return persian;
        }
        if (portuguese != null) {
            return portuguese;
        }
        if (romanian != null) {
            return romanian;
        }
        if (russian != null) {
            return russian;
        }
        if (spanish != null) {
            return spanish;
        }
        if (swedish != null) {
            return swedish;
        }
        if (turkish != null) {
            return turkish;
        }
        if (thai != null) {
            return thai;
        }
        //
        return this.unknown;
    }

    public String getUnknown() {
        return this.unknown;
    }

    public void setUnknown(String unknown) {
        this.unknown = unknown;
    }

    public String getArabic() {
        return this.arabic;
    }

    public void setArabic(String arabic) {
        this.arabic = arabic;
    }

    public String getArmenian() {
        return this.armenian;
    }

    public void setArmenian(String armenian) {
        this.armenian = armenian;
    }

    public String getBasque() {
        return this.basque;
    }

    public void setBasque(String basque) {
        this.basque = basque;
    }

    public String getBengali() {
        return this.bengali;
    }

    public void setBengali(String bengali) {
        this.bengali = bengali;
    }

    public String getBulgarian() {
        return this.bulgarian;
    }

    public void setBulgarian(String bulgarian) {
        this.bulgarian = bulgarian;
    }

    public String getCatalan() {
        return this.catalan;
    }

    public void setCatalan(String catalan) {
        this.catalan = catalan;
    }

    public String getCzech() {
        return this.czech;
    }

    public void setCzech(String czech) {
        this.czech = czech;
    }

    public String getDanish() {
        return this.danish;
    }

    public void setDanish(String danish) {
        this.danish = danish;
    }

    public String getDutch() {
        return this.dutch;
    }

    public void setDutch(String dutch) {
        this.dutch = dutch;
    }

    public String getEnglish() {
        return this.english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public String getEstonian() {
        return this.estonian;
    }

    public void setEstonian(String estonian) {
        this.estonian = estonian;
    }

    public String getFinnish() {
        return this.finnish;
    }

    public void setFinnish(String finnish) {
        this.finnish = finnish;
    }

    public String getFrench() {
        return this.french;
    }

    public void setFrench(String french) {
        this.french = french;
    }

    public String getGalician() {
        return this.galician;
    }

    public void setGalician(String galician) {
        this.galician = galician;
    }

    public String getGerman() {
        return this.german;
    }

    public void setGerman(String german) {
        this.german = german;
    }

    public String getGreek() {
        return this.greek;
    }

    public void setGreek(String greek) {
        this.greek = greek;
    }

    public String getHindi() {
        return this.hindi;
    }

    public void setHindi(String hindi) {
        this.hindi = hindi;
    }

    public String getHungarian() {
        return this.hungarian;
    }

    public void setHungarian(String hungarian) {
        this.hungarian = hungarian;
    }

    public String getIndonesian() {
        return this.indonesian;
    }

    public void setIndonesian(String indonesian) {
        this.indonesian = indonesian;
    }

    public String getIrish() {
        return this.irish;
    }

    public void setIrish(String irish) {
        this.irish = irish;
    }

    public String getItalian() {
        return this.italian;
    }

    public void setItalian(String italian) {
        this.italian = italian;
    }

    public String getLatvian() {
        return this.latvian;
    }

    public void setLatvian(String latvian) {
        this.latvian = latvian;
    }

    public String getLithuanian() {
        return this.lithuanian;
    }

    public void setLithuanian(String lithuanian) {
        this.lithuanian = lithuanian;
    }

    public String getNorwegian() {
        return this.norwegian;
    }

    public void setNorwegian(String norwegian) {
        this.norwegian = norwegian;
    }

    public String getPersian() {
        return this.persian;
    }

    public void setPersian(String persian) {
        this.persian = persian;
    }

    public String getPortuguese() {
        return this.portuguese;
    }

    public void setPortuguese(String portuguese) {
        this.portuguese = portuguese;
    }

    public String getRomanian() {
        return this.romanian;
    }

    public void setRomanian(String romanian) {
        this.romanian = romanian;
    }

    public String getRussian() {
        return this.russian;
    }

    public void setRussian(String russian) {
        this.russian = russian;
    }

    public String getSpanish() {
        return this.spanish;
    }

    public void setSpanish(String spanish) {
        this.spanish = spanish;
    }

    public String getSwedish() {
        return this.swedish;
    }

    public void setSwedish(String swedish) {
        this.swedish = swedish;
    }

    public String getTurkish() {
        return this.turkish;
    }

    public void setTurkish(String turkish) {
        this.turkish = turkish;
    }

    public String getThai() {
        return this.thai;
    }

    public void setThai(String thai) {
        this.thai = thai;
    }

    public List<String> getValues() {
        return this.values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

    public void operate(UnaryOperator<String> i) {
        if (values != null) values = values.stream().map(i).filter(Objects::nonNull).collect(Collectors.toList());

        if (unknown != null) unknown = i.apply(unknown);

        if (arabic != null) arabic = i.apply(arabic);
        if (armenian != null) armenian = i.apply(armenian);
        if (basque != null) basque = i.apply(basque);
        if (bengali != null) bengali = i.apply(bengali);
        if (bulgarian != null) bulgarian = i.apply(bulgarian);
        if (catalan != null) catalan = i.apply(catalan);
        if (czech != null) czech = i.apply(czech);
        if (danish != null) danish = i.apply(danish);
        if (dutch != null) dutch = i.apply(dutch);
        if (english != null) english = i.apply(english);
        if (estonian != null) estonian = i.apply(estonian);
        if (finnish != null) finnish = i.apply(finnish);
        if (french != null) french = i.apply(french);
        if (galician != null) galician = i.apply(galician);
        if (german != null) german = i.apply(german);
        if (greek != null) greek = i.apply(greek);
        if (hindi != null) hindi = i.apply(hindi);
        if (hungarian != null) hungarian = i.apply(hungarian);
        if (indonesian != null) indonesian = i.apply(indonesian);
        if (irish != null) irish = i.apply(irish);
        if (italian != null) italian = i.apply(italian);
        if (latvian != null) latvian = i.apply(latvian);
        if (lithuanian != null) lithuanian = i.apply(lithuanian);
        if (norwegian != null) norwegian = i.apply(norwegian);
        if (persian != null) persian = i.apply(persian);
        if (portuguese != null) portuguese = i.apply(portuguese);
        if (romanian != null) romanian = i.apply(romanian);
        if (russian != null) russian = i.apply(russian);
        if (spanish != null) spanish = i.apply(spanish);
        if (swedish != null) swedish = i.apply(swedish);
        if (turkish != null) turkish = i.apply(turkish);
        if (thai != null) thai = i.apply(thai);
    }
}
