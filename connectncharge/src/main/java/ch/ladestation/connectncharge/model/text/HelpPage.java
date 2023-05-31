package ch.ladestation.connectncharge.model.text;

public enum HelpPage {
    WELCOME("Herzlich willkommen im Hilfe-Menü!\n\n"
            + "Hier erhalten Sie alle wichtigen Informationen, um im Spiel erfolgreich zu sein. "
            + "Bitte wählen Sie einen der folgenden Abschnitte, um detaillierte Informationen "
            + "und hilfreiche Tipps zu bekommen:", null),
    GAME_START("Spielstart: \n\nUm das Spiel zu beginnen, klicken Sie zunächst auf den "
            + "Ladebildschirm (Connect 'n Charge),"
            + " um zum Hauptbildschirm zu gelangen (falls Sie sich noch im Ladebildschirm befinden). "
            + "Anschließend klicken Sie auf den \"Start\"-Button in der unteren Mitte "
            + "des Halbkreises, um den Countdown zu starten und das Spiel zu beginnen. "
            + "Im Burger-Menü können Sie zudem ein Bonuslevel auswählen und spielen.", WELCOME),
    GAME_CONTROLS("Spielsteuerung: \n\nUm das Spiel zu steuern, tippen Sie einmal auf die "
            + "weißen Kanten (Streifen), um sie blau aufleuchten zu lassen. Tippen Sie erneut auf "
            + "dieselbe Kante, erlischt die Farbe wieder. "
            + "Das Display kann per Touch bedient werden, um Tipps abzurufen, den Hilfe-Bildschirm "
            + "aufzurufen oder das Spiel zu verlassen.", GAME_START),
    GAME_PLAY("Gameplay:\n\nSobald das Spiel startet, beginnt die Zeit zu laufen. Wenn Sie Kanten auswählen, "
            + "werden deren Kosten auf dem Display angezeigt. Ihr Ziel ist es, durch geschicktes Kombinieren "
            + "und Ausprobieren der Kantenlängen, "
            + "den kürzesten und kosteneffizientesten Weg zu finden, um alle blauleuchtende Ferienhäuser mit "
            + "Ladestationen zu verbinden.", GAME_CONTROLS),
    RULES("Regeln: \n\nDas Spiel endet erst, wenn der kosteneffizienteste Weg gefunden "
            + "und die LEDs wieder erloschen sind. "
            + "Ein Kreis im Graphen ist nicht erlaubt - falls Sie dennoch einen erstellen, erhalten Sie "
            + "eine entsprechende Meldung auf dem Display.", GAME_PLAY),
    GOAL("Ziel:\n\nIhr Ziel ist es, so schnell wie möglich den kostengünstigsten und "
            + "kürzesten Weg im Graphen zu finden. "
            + "Sobald der optimale Weg gefunden und verbunden wurde, ist das Spiel beendet, und "
            + "Ihre Zeit wird in der Rangliste gespeichert.", RULES),
    SUPPORT("Support:\n\nSollten Sie auf technische Probleme oder Fehler im Spiel stoßen, "
            + "wenden Sie sich bitte an die zuständige Aufsichtsperson.", GOAL);

    private final String text;
    private final HelpPage previousPage;

    HelpPage(String text, HelpPage previousPage) {
        this.text = text;
        this.previousPage = previousPage;
    }

    public String getText() {
        return text;
    }

    public HelpPage getPreviousPage() {
        return previousPage;
    }

    public HelpPage getNext() {
        if (this.ordinal() == values().length - 1) {
            return values()[0];
        }
        return values()[this.ordinal() + 1];
    }

    public HelpPage getPrevious() {
        if (this.ordinal() == 0) {
            return values()[values().length - 1];
        }
        return values()[this.ordinal() - 1];
    }
}
