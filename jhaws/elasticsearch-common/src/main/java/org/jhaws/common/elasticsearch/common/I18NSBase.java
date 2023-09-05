package org.jhaws.common.elasticsearch.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

// @JsonUnwrapped(prefix = "${FIELDNAME}_")
// @NestedField
@SuppressWarnings("serial")
public class I18NSBase implements Serializable {
    public static final String UNKNOWN = "unknown";

    public static final String VALUES = "values";

    public static final String SUFFIX_SORTABLE_EXTENDED = ".sortableExtended";

    @Field(type = FieldType.TEXT, fielddata = Bool.TRUE, customAnalyzer = Analyzers.CUSTOM_SORTABLE_ONLY_ALPHANUMERIC_ANALYZER)
    @FieldExtra(value = { @Field(name = "sortableExtended", type = FieldType.TEXT, fielddata = Bool.TRUE, customAnalyzer = Analyzers.CUSTOM_SORTABLE_EXTENDED_ALPHANUMERIC_ANALYZER) })
    protected List<String> values = new ArrayList<>();

    @Field(type = FieldType.TEXT, customAnalyzer = Analyzers.CUSTOM_ANY_LANGUAGE_ANALYZER)
    protected List<String> unknown;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.arabic)
    protected List<String> arabic;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.armenian)
    protected List<String> armenian;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.basque)
    protected List<String> basque;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.bengali)
    protected List<String> bengali;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.bulgarian)
    protected List<String> bulgarian;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.catalan)
    protected List<String> catalan;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.czech)
    protected List<String> czech;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.danish)
    protected List<String> danish;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.dutch)
    protected List<String> dutch;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.english)
    protected List<String> english;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.estonian)
    protected List<String> estonian;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.finnish)
    protected List<String> finnish;

    @Field(type = FieldType.TEXT, customAnalyzer = Analyzers.CUSTOM_FRENCH_LANGUAGE_ANALYZER)
    // @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.french)
    protected List<String> french;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.galician)
    protected List<String> galician;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.german)
    protected List<String> german;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.greek)
    protected List<String> greek;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.hindi)
    protected List<String> hindi;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.hungarian)
    protected List<String> hungarian;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.indonesian)
    protected List<String> indonesian;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.irish)
    protected List<String> irish;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.italian)
    protected List<String> italian;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.latvian)
    protected List<String> latvian;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.lithuanian)
    protected List<String> lithuanian;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.norwegian)
    protected List<String> norwegian;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.persian)
    protected List<String> persian;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.portuguese)
    protected List<String> portuguese;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.romanian)
    protected List<String> romanian;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.russian)
    protected List<String> russian;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.spanish)
    protected List<String> spanish;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.swedish)
    protected List<String> swedish;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.turkish)
    protected List<String> turkish;

    @Field(type = FieldType.TEXT, analyzer = Analyzer.language, language = Language.thai)
    protected List<String> thai;

    public I18NSBase() {
        super();
    }

    public I18NSBase(String iso, String... values0) {
        addValues(iso, values0);
    }

    public I18NSBase(Language language, String... values0) {
        addValues(language, values0);
    }

    public I18NSBase(Language language, Collection<String> values0) {
        addValues(language, values0);
    }

    @Override
    public String toString() {
        return String.valueOf(getValues());
    }

    public SortedSet<String> collect() {
        SortedSet<String> set = new TreeSet<>();

        if (unknown != null) set.addAll(unknown);

        if (arabic != null) set.addAll(arabic);
        if (armenian != null) set.addAll(armenian);
        if (basque != null) set.addAll(basque);
        if (bengali != null) set.addAll(bengali);
        if (bulgarian != null) set.addAll(bulgarian);
        if (catalan != null) set.addAll(catalan);
        if (czech != null) set.addAll(czech);
        if (danish != null) set.addAll(danish);
        if (dutch != null) set.addAll(dutch);
        if (english != null) set.addAll(english);
        if (estonian != null) set.addAll(estonian);
        if (finnish != null) set.addAll(finnish);
        if (french != null) set.addAll(french);
        if (galician != null) set.addAll(galician);
        if (german != null) set.addAll(german);
        if (greek != null) set.addAll(greek);
        if (hindi != null) set.addAll(hindi);
        if (hungarian != null) set.addAll(hungarian);
        if (indonesian != null) set.addAll(indonesian);
        if (irish != null) set.addAll(irish);
        if (italian != null) set.addAll(italian);
        if (latvian != null) set.addAll(latvian);
        if (lithuanian != null) set.addAll(lithuanian);
        if (norwegian != null) set.addAll(norwegian);
        if (persian != null) set.addAll(persian);
        if (portuguese != null) set.addAll(portuguese);
        if (romanian != null) set.addAll(romanian);
        if (russian != null) set.addAll(russian);
        if (spanish != null) set.addAll(spanish);
        if (swedish != null) set.addAll(swedish);
        if (turkish != null) set.addAll(turkish);
        if (thai != null) set.addAll(thai);

        return set;
    }

    public void distinct() {
        if (arabic != null && arabic.size() > 1) arabic = arabic.stream().distinct().collect(Collectors.toList());
        if (armenian != null && armenian.size() > 1) armenian = armenian.stream().distinct().collect(Collectors.toList());
        if (basque != null && basque.size() > 1) basque = basque.stream().distinct().collect(Collectors.toList());
        if (bengali != null && bengali.size() > 1) bengali = bengali.stream().distinct().collect(Collectors.toList());
        if (bulgarian != null && bulgarian.size() > 1) bulgarian = bulgarian.stream().distinct().collect(Collectors.toList());
        if (catalan != null && catalan.size() > 1) catalan = catalan.stream().distinct().collect(Collectors.toList());
        if (czech != null && czech.size() > 1) czech = czech.stream().distinct().collect(Collectors.toList());
        if (danish != null && danish.size() > 1) danish = danish.stream().distinct().collect(Collectors.toList());
        if (dutch != null && dutch.size() > 1) dutch = dutch.stream().distinct().collect(Collectors.toList());
        if (english != null && english.size() > 1) english = english.stream().distinct().collect(Collectors.toList());
        if (estonian != null && estonian.size() > 1) estonian = estonian.stream().distinct().collect(Collectors.toList());
        if (finnish != null && finnish.size() > 1) finnish = finnish.stream().distinct().collect(Collectors.toList());
        if (french != null && french.size() > 1) french = french.stream().distinct().collect(Collectors.toList());
        if (galician != null && galician.size() > 1) galician = galician.stream().distinct().collect(Collectors.toList());
        if (german != null && german.size() > 1) german = german.stream().distinct().collect(Collectors.toList());
        if (greek != null && greek.size() > 1) greek = greek.stream().distinct().collect(Collectors.toList());
        if (hindi != null && hindi.size() > 1) hindi = hindi.stream().distinct().collect(Collectors.toList());
        if (hungarian != null && hungarian.size() > 1) hungarian = hungarian.stream().distinct().collect(Collectors.toList());
        if (indonesian != null && indonesian.size() > 1) indonesian = indonesian.stream().distinct().collect(Collectors.toList());
        if (irish != null && irish.size() > 1) irish = irish.stream().distinct().collect(Collectors.toList());
        if (italian != null && italian.size() > 1) italian = italian.stream().distinct().collect(Collectors.toList());
        if (latvian != null && latvian.size() > 1) latvian = latvian.stream().distinct().collect(Collectors.toList());
        if (lithuanian != null && lithuanian.size() > 1) lithuanian = lithuanian.stream().distinct().collect(Collectors.toList());
        if (norwegian != null && norwegian.size() > 1) norwegian = norwegian.stream().distinct().collect(Collectors.toList());
        if (persian != null && persian.size() > 1) persian = persian.stream().distinct().collect(Collectors.toList());
        if (portuguese != null && portuguese.size() > 1) portuguese = portuguese.stream().distinct().collect(Collectors.toList());
        if (romanian != null && romanian.size() > 1) romanian = romanian.stream().distinct().collect(Collectors.toList());
        if (russian != null && russian.size() > 1) russian = russian.stream().distinct().collect(Collectors.toList());
        if (spanish != null && spanish.size() > 1) spanish = spanish.stream().distinct().collect(Collectors.toList());
        if (swedish != null && swedish.size() > 1) swedish = swedish.stream().distinct().collect(Collectors.toList());
        if (turkish != null && turkish.size() > 1) turkish = turkish.stream().distinct().collect(Collectors.toList());
        if (thai != null && thai.size() > 1) thai = thai.stream().distinct().collect(Collectors.toList());
        //
        if (unknown != null && unknown.size() > 1) unknown = unknown.stream().distinct().collect(Collectors.toList());
    }

    public void resetValues() {
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

    public void addValues(String languageIso, Collection<String> values0) {
        if (values0 != null && !values0.isEmpty()) values0.stream().forEach(value -> {
            Language lang = Language.forIso(languageIso).orElse(null);
            value(lang, value);
        });
    }

    public void addValues(Language language, Collection<String> values0) {
        if (values0 != null && !values0.isEmpty()) values0.stream().forEach(value -> value(language, value));
    }

    public void addValues(String languageIso, String... values0) {
        if (values0 != null && values0.length > 0) Arrays.stream(values0).forEach(value -> {
            Language lang = Language.forIso(languageIso).orElse(null);
            value(lang, value);
        });
    }

    public void addValues(Language language, String... values0) {
        if (values0 != null && values0.length > 0) Arrays.stream(values0).forEach(value -> value(language, value));
    }

    public void addValue(String languageIso, String value) {
        value(Language.forIso(languageIso).orElse(null), value);
    }

    public void addValue(Language language, String value) {
        value(language, value);
    }

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
            default:
                if (unknown == null) unknown = new ArrayList<>();
                unknown.add(value);
                break;
        }
    }

    public List<String> getValue(Language lang) {
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

    public List<String> getValues() {
        return this.values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

    public void operate(UnaryOperator<List<String>> i) {
        if (values != null) values = i.apply(values);

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
