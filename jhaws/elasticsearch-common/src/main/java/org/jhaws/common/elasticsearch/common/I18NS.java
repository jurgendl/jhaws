package org.jhaws.common.elasticsearch.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

// @JsonUnwrapped(prefix = "${FIELDNAME}_")
// @NestedField
@SuppressWarnings("serial")
public class I18NS implements Serializable {
    @Field(type = FieldType.TEXT, customAnalyzer = Analyzers.CUSTOM_ANY_LANGUAGE_ANALYZER)
    private List<String> unknown;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.arabic)
    private List<String> arabic;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.armenian)
    private List<String> armenian;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.basque)
    private List<String> basque;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.bengali)
    private List<String> bengali;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.bulgarian)
    private List<String> bulgarian;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.catalan)
    private List<String> catalan;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.czech)
    private List<String> czech;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.danish)
    private List<String> danish;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.dutch)
    private List<String> dutch;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.english)
    private List<String> english;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.estonian)
    private List<String> estonian;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.finnish)
    private List<String> finnish;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.french)
    private List<String> french;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.galician)
    private List<String> galician;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.german)
    private List<String> german;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.greek)
    private List<String> greek;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.hindi)
    private List<String> hindi;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.hungarian)
    private List<String> hungarian;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.indonesian)
    private List<String> indonesian;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.irish)
    private List<String> irish;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.italian)
    private List<String> italian;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.latvian)
    private List<String> latvian;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.lithuanian)
    private List<String> lithuanian;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.norwegian)
    private List<String> norwegian;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.persian)
    private List<String> persian;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.portuguese)
    private List<String> portuguese;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.romanian)
    private List<String> romanian;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.russian)
    private List<String> russian;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.spanish)
    private List<String> spanish;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.swedish)
    private List<String> swedish;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.turkish)
    private List<String> turkish;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.thai)
    private List<String> thai;

    //

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.japanese)
    private List<String> japanese;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.chinese)
    private List<String> chinese;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.korean)
    private List<String> korean;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.ukrainian)
    private List<String> ukrainian;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.polish)
    private List<String> polish;

    public I18NS() {
        super();
    }

    public I18NS(String iso, String... values) {
        addValues(iso, values);
    }

    public I18NS(Language language, String... values) {
        addValues(language, values);
    }

    @Override
    public String toString() {
        return String.valueOf(getValue());
    }

    public I18NS resetValues() {
        unknown = null;
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
            //
            japanese = null;
            chinese = null;
            korean = null;
            ukrainian = null;
            polish = null;
        }
        return this;
    }

    public I18NS addValues(String iso, String... values) {
        Language lan = StringUtils.isBlank(iso) ? null
                : Arrays.stream(Language.values()).filter(l -> l.iso() != null).filter(l -> l.iso().equalsIgnoreCase(iso)).findAny().orElse(null);
        return addValues(lan, values);
    }

    public I18NS addValue(String iso, String value) {
        Language lan = StringUtils.isBlank(iso) ? null
                : Arrays.stream(Language.values()).filter(l -> l.iso() != null).filter(l -> l.iso().equalsIgnoreCase(iso)).findAny().orElse(null);
        addValue(lan, value);
        return this;
    }

    public I18NS addValues(Language language, String... values) {
        Arrays.stream(values).forEach(value -> addValue(language, value));
        return this;
    }

    public I18NS addValue(Language language, String value) {
        if (StringUtils.isBlank(value)) throw new NullPointerException();
        value(language, value);
        return this;
    }

    private void value(Language language, String value) {
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
            case brazilian:
            case cjk:
            case sorani:
            case uninitialized:
            default:
                if (unknown == null) unknown = new ArrayList<>();
                unknown.add(value);
                break;
        }
    }

    public List<String> getValue() {
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
        return this.unknown;
    }

    public List<String> getUnknown() {
        return this.unknown;
    }

    public void setUnknown(List<String> unknown) {
        this.unknown = unknown;
    }

    public List<String> getArabic() {
        return this.arabic;
    }

    public void setArabic(List<String> arabic) {
        this.arabic = arabic;
    }

    public List<String> getArmenian() {
        return this.armenian;
    }

    public void setArmenian(List<String> armenian) {
        this.armenian = armenian;
    }

    public List<String> getBasque() {
        return this.basque;
    }

    public void setBasque(List<String> basque) {
        this.basque = basque;
    }

    public List<String> getBengali() {
        return this.bengali;
    }

    public void setBengali(List<String> bengali) {
        this.bengali = bengali;
    }

    public List<String> getBulgarian() {
        return this.bulgarian;
    }

    public void setBulgarian(List<String> bulgarian) {
        this.bulgarian = bulgarian;
    }

    public List<String> getCatalan() {
        return this.catalan;
    }

    public void setCatalan(List<String> catalan) {
        this.catalan = catalan;
    }

    public List<String> getCzech() {
        return this.czech;
    }

    public void setCzech(List<String> czech) {
        this.czech = czech;
    }

    public List<String> getDanish() {
        return this.danish;
    }

    public void setDanish(List<String> danish) {
        this.danish = danish;
    }

    public List<String> getDutch() {
        return this.dutch;
    }

    public void setDutch(List<String> dutch) {
        this.dutch = dutch;
    }

    public List<String> getEnglish() {
        return this.english;
    }

    public void setEnglish(List<String> english) {
        this.english = english;
    }

    public List<String> getEstonian() {
        return this.estonian;
    }

    public void setEstonian(List<String> estonian) {
        this.estonian = estonian;
    }

    public List<String> getFinnish() {
        return this.finnish;
    }

    public void setFinnish(List<String> finnish) {
        this.finnish = finnish;
    }

    public List<String> getFrench() {
        return this.french;
    }

    public void setFrench(List<String> french) {
        this.french = french;
    }

    public List<String> getGalician() {
        return this.galician;
    }

    public void setGalician(List<String> galician) {
        this.galician = galician;
    }

    public List<String> getGerman() {
        return this.german;
    }

    public void setGerman(List<String> german) {
        this.german = german;
    }

    public List<String> getGreek() {
        return this.greek;
    }

    public void setGreek(List<String> greek) {
        this.greek = greek;
    }

    public List<String> getHindi() {
        return this.hindi;
    }

    public void setHindi(List<String> hindi) {
        this.hindi = hindi;
    }

    public List<String> getHungarian() {
        return this.hungarian;
    }

    public void setHungarian(List<String> hungarian) {
        this.hungarian = hungarian;
    }

    public List<String> getIndonesian() {
        return this.indonesian;
    }

    public void setIndonesian(List<String> indonesian) {
        this.indonesian = indonesian;
    }

    public List<String> getIrish() {
        return this.irish;
    }

    public void setIrish(List<String> irish) {
        this.irish = irish;
    }

    public List<String> getItalian() {
        return this.italian;
    }

    public void setItalian(List<String> italian) {
        this.italian = italian;
    }

    public List<String> getLatvian() {
        return this.latvian;
    }

    public void setLatvian(List<String> latvian) {
        this.latvian = latvian;
    }

    public List<String> getLithuanian() {
        return this.lithuanian;
    }

    public void setLithuanian(List<String> lithuanian) {
        this.lithuanian = lithuanian;
    }

    public List<String> getNorwegian() {
        return this.norwegian;
    }

    public void setNorwegian(List<String> norwegian) {
        this.norwegian = norwegian;
    }

    public List<String> getPersian() {
        return this.persian;
    }

    public void setPersian(List<String> persian) {
        this.persian = persian;
    }

    public List<String> getPortuguese() {
        return this.portuguese;
    }

    public void setPortuguese(List<String> portuguese) {
        this.portuguese = portuguese;
    }

    public List<String> getRomanian() {
        return this.romanian;
    }

    public void setRomanian(List<String> romanian) {
        this.romanian = romanian;
    }

    public List<String> getRussian() {
        return this.russian;
    }

    public void setRussian(List<String> russian) {
        this.russian = russian;
    }

    public List<String> getSpanish() {
        return this.spanish;
    }

    public void setSpanish(List<String> spanish) {
        this.spanish = spanish;
    }

    public List<String> getSwedish() {
        return this.swedish;
    }

    public void setSwedish(List<String> swedish) {
        this.swedish = swedish;
    }

    public List<String> getTurkish() {
        return this.turkish;
    }

    public void setTurkish(List<String> turkish) {
        this.turkish = turkish;
    }

    public List<String> getThai() {
        return this.thai;
    }

    public void setThai(List<String> thai) {
        this.thai = thai;
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
}
