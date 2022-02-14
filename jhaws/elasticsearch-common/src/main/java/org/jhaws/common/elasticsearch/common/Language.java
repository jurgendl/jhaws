package org.jhaws.common.elasticsearch.common;

import java.util.Arrays;
import java.util.Optional;

// https://www.elastic.co/guide/en/machine-learning/7.6/ml-lang-ident.html
public enum Language {
    uninitialized(null) //
    //
    //
    , arabic("ar")//
    , armenian("hy")//
    , basque("eu")//
    , bengali("nb")//
    , brazilian(null)//
    , bulgarian("bg")//
    , catalan("ca")//
    , cjk(null)// CJK characters is a collective term for the Chinese, Japanese, and Korean languages
    , czech("cs")//
    , danish("da")//
    , dutch("nl")//
    , english("en")//
    , estonian("et")//
    , finnish("fi")//
    , french("fr")//
    , galician("gl")//
    , german("de")//
    , greek("el")//
    , hindi("hi")//
    , hungarian("hu")//
    , indonesian("id")//
    , irish("ga")//
    , italian("it")//
    , latvian("lv")//
    , lithuanian("lt")//
    , norwegian("no")//
    , persian("fa")//
    , portuguese("pt")//
    , romanian("ro")//
    , russian("ru")//
    , sorani(null)// Koerdisch dialect
    , spanish("es")//
    , swedish("sv")//
    , turkish("tr")//
    , thai("th")//
    //
    , japanese("ja")//
    , chinese("zh")//
    , korean("ko")//
    , ukrainian("uk")//
    , polish("pl"),//
    ;

    private String id;

    private String iso;

    private Language(String iso) {
        this.id = name();
        this.iso = iso;
    }

    private Language(String id, String iso) {
        this.id = id;
        this.iso = iso;
    }

    public String id() {
        return id;
    }

    public String iso() {
        return iso;
    }

    public static Optional<Language> forIso(String iso) {
        return iso == null ? Optional.empty() : Arrays.stream(Language.values()).filter(l -> l.iso() != null).filter(l -> iso.equalsIgnoreCase(l.iso())).findAny();
    }
}
