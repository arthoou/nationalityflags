package com.arthou.nationalityflags;

import java.util.LinkedHashMap;
import java.util.Map;

public enum FlagCountry {
    ALGERIA("Algeria", "Algeria", "\ue901"),
    ANDORRA("Andorra", "Andorra", "\ue902"),
    ARGENTINA("Argentina", "Argentina", "\ue903"),
    AUSTRALIA("Australia", "Australia", "\ue904"),
    BOLIVIA("Bolivia", "Bolivia", "\ue905"),
    BRASIL("Brasil", "Brazil", "\ue906"),
    CANADA("Canada", "Canada", "\ue907"),
    CHILE("Chile", "Chile", "\ue908"),
    COLOMBIA("Colombia", "Colombia", "\ue909"),
    DRC("Drc", "Democratic Republic of the Congo", "\ue910"),
    INGLATERRA("Inglaterra", "England", "\ue911"),
    FINLANDIA("Finlandia", "Finland", "\ue912"),
    FRANCA("Franca", "France", "\ue913"),
    ALEMANHA("Alemanha", "Germany", "\ue914"),
    HONDURAS("Honduras", "Honduras", "\ue915"),
    INDIA("India", "India", "\ue916"),
    ITALIA("Italia", "Italy", "\ue917"),
    JAPAO("Japao", "Japan", "\ue918"),
    MEXICO("Mexico", "Mexico", "\ue919"),
    PAISES("Paises", "Netherlands", "\ue920"),
    NORUEGA("Noruega", "Norway", "\ue921"),
    PERU("Peru", "Peru", "\ue922"),
    PORTUGAL("Portugal", "Portugal", "\ue923"),
    PORTO("Porto", "Puerto Rico", "\ue924"),
    COREIA("Coreia", "South Korea", "\ue925"),
    ESPANHA("Espanha", "Spain", "\ue926"),
    SUICA("Suica", "Switzerland", "\ue927"),
    REINO("Reino", "United Kingdom", "\ue928"),
    EUA("Eua", "United States", "\ue929"),
    VENEZUELA("Venezuela", "Venezuela", "\ue930"),
    RUSSIA("Russia", "Russia", "\ue931"),
    GRECIA("Grecia", "Greece", "\ue932"),
    UCRANIA("Ucrania", "Ukraine", "\ue933"),
    POLONIA("Polonia", "Poland", "\ue934"),
    CHINA("China", "China", "\ue935"),
    TURQUIA("Turquia", "Turkey", "\ue936"),
    PARAGUAI("Paraguai", "Paraguay", "\ue937"),
    URUGUAI("Uruguai", "Uruguay", "\ue938"),
    FILIPINAS("Filipinas", "Philippines", "\ue939"),
    LETONIA("Letonia", "Latvia", "\ue940"),
    TCHEQUIA("Tchequia", "Czechia", "\ue941"),
    INDONESIA("Indonesia", "Indonesia", "\ue942"),
    AUSTRIA("Austria", "Austria", "\ue943"),
    GUATEMALA("Guatemala", "Guatemala", "\ue944"),
    ROMENIA("Romenia", "Romania", "\ue945"),
    CROACIA("Croacia", "Croatia", "\ue946"),
    BELGICA("Belgica", "Belgium", "\ue947"),
    AFRICA_SUL("AfricaSul", "South Africa", "\ue948"),
    TAILANDIA("Tailandia", "Thailand", "\ue949"),
    DINAMARCA("Dinamarca", "Denmark", "\ue94a"),
    SUECIA("Suecia", "Sweden", "\ue94b"),
    IRLANDA("Irlanda", "Ireland", "\ue94c"),
    BANGLADESH("Bangladesh", "Bangladesh", "\ue94d"),
    VIETNA("Vietna", "Vietnam", "\ue94e"),
    EGITO("Egito", "Egypt", "\ue94f"),
    SINGAPURA("Singapura", "Singapore", "\ue950"),
    MALASIA("Malasia", "Malaysia", "\ue951"),
    ARABIA_SAUDITA("ArabiaSaudita", "Saudi Arabia", "\ue952"),
    NIGERIA("Nigeria", "Nigeria", "\ue953"),
    GANA("Gana", "Ghana", "\ue954");

    public static final FlagCountry[] PAGE_1 = {
        ALGERIA, ANDORRA, ARGENTINA, AUSTRALIA, BOLIVIA, BRASIL,
        CANADA, CHILE, COLOMBIA, DRC, INGLATERRA, FINLANDIA,
        FRANCA, ALEMANHA, HONDURAS, INDIA, ITALIA, JAPAO,
        MEXICO, PAISES, NORUEGA, PERU, PORTUGAL, PORTO,
        COREIA, ESPANHA, SUICA, REINO, EUA, VENEZUELA
    };

    public static final FlagCountry[] PAGE_2 = {
        LETONIA, TCHEQUIA, INDONESIA, AUSTRIA, GUATEMALA, ROMENIA,
        CROACIA, BELGICA, AFRICA_SUL, UCRANIA, RUSSIA, POLONIA,
        GRECIA, CHINA, TURQUIA, PARAGUAI, URUGUAI, TAILANDIA,
        DINAMARCA, SUECIA, IRLANDA, BANGLADESH, VIETNA, EGITO,
        SINGAPURA, MALASIA, FILIPINAS, ARABIA_SAUDITA, NIGERIA, GANA
    };

    private static final Map<String, FlagCountry> BY_ID = new LinkedHashMap<>();

    static {
        for (FlagCountry country : values()) {
            BY_ID.put(country.id, country);
        }
    }

    public final String id;
    public final String englishName;
    public final String glyph;

    FlagCountry(String id, String englishName, String glyph) {
        this.id = id;
        this.englishName = englishName;
        this.glyph = glyph;
    }

    public static FlagCountry byId(String id) {
        return BY_ID.get(id);
    }
}
