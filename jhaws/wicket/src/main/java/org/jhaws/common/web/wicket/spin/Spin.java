package org.jhaws.common.web.wicket.spin;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.jhaws.common.web.wicket.CssResourceReference;

// http://www.raphaelfabeni.com.br/css-loader/
// https://github.com/raphaelfabeni/css-loader
// https://tobiasahlin.com/spinkit/
//
// response.render(CssHeaderItem.forReference(Spin.css(WicketApplication.get().getSettings().getSpinner())));
//
// <span wicket:id="spinnercontainer" id="spinnercontainer" class="invisible">
// <div wicket:id="spinner" class=""></div>
// </span>
//
// addSpinner(html, "spinnercontainer", "spinner", WicketApplication.get().getSettings().getSpinner());
//
// protected void addSpinner(MarkupContainer html, String spinnercontainer, String spinner, String spinnerType) {...
// }
public class Spin {
    public static final String HIDE = ";$('#spinnercontainer').removeClass('visible').addClass('invisible');";

    public static final String SHOW = ";$('#spinnercontainer').removeClass('invisible').addClass('visible');";

    public static enum SpinType implements Supplier<String> {
        _default("default"), //
        ball(null), //
        bar(null), //
        bar_ping_pong("bar-ping-pong"), //
        border(null), //
        bouncing(null), //
        clock(null), //
        curtain(null), //
        _double("double"), //
        music(null), //
        pokeball(null), //
        smartphone(null), //
        sk_square("sk-square"), //
        sk_chase("sk-chase"), //
        sk_spinner("sk-spinner"), //
        sk_rect("sk-rect"), //
        sk_bounce("sk-bounce"), //
        sk_circle("sk-circle"), //
        sk_cube_grid("sk-cube-grid"), //
        sk_fading_circle("sk-fading-circle"), //
        sk_folding_cube("sk-folding-cube"),//
        //
        ;

        private final String id;

        private SpinType(String id) {
            this.id = id;
        }

        public String id() {
            return id == null ? name() : id;
        }

        @Override
        public String get() {
            return id();
        }
    }

    public static List<String> types = Arrays.stream(SpinType.values()).map(SpinType::get).collect(Collectors.toList());

    public static CssResourceReference SPIN_CSS_BALL = new CssResourceReference(Spin.class, "loader-ball.css");

    public static CssResourceReference SPIN_CSS_BAR = new CssResourceReference(Spin.class, "loader-bar.css");

    public static CssResourceReference SPIN_CSS_PING_PONG = new CssResourceReference(Spin.class, "loader-bar-ping-pong.css");

    public static CssResourceReference SPIN_CSS_BORDER = new CssResourceReference(Spin.class, "loader-border.css");

    public static CssResourceReference SPIN_CSS_BOUNCING = new CssResourceReference(Spin.class, "loader-bouncing.css");

    public static CssResourceReference SPIN_CSS_CLOCK = new CssResourceReference(Spin.class, "loader-clock.css");

    public static CssResourceReference SPIN_CSS_CURTAIN = new CssResourceReference(Spin.class, "loader-curtain.css");

    public static CssResourceReference SPIN_CSS_DEFAULT = new CssResourceReference(Spin.class, "loader-default.css");

    public static CssResourceReference SPIN_CSS_DOULE = new CssResourceReference(Spin.class, "loader-double.css");

    public static CssResourceReference SPIN_CSS_MUSIC = new CssResourceReference(Spin.class, "loader-music.css");

    public static CssResourceReference SPIN_CSS_POKEBALL = new CssResourceReference(Spin.class, "loader-pokeball.css");

    public static CssResourceReference SPIN_CSS_CELL = new CssResourceReference(Spin.class, "loader-smartphone.css");

    public static CssResourceReference SPINKIT_SQUARE = new CssResourceReference(Spin.class, "sk-square.css");

    public static CssResourceReference SPINKIT_CHASE = new CssResourceReference(Spin.class, "sk-chase.css");

    public static CssResourceReference SPINKIT_SPINNER = new CssResourceReference(Spin.class, "sk-spinner.css");

    public static CssResourceReference SPINKIT_RECT = new CssResourceReference(Spin.class, "sk-rect.css");

    public static CssResourceReference SPINKIT_BOUNCE = new CssResourceReference(Spin.class, "sk-bounce.css");

    public static CssResourceReference SPINKIT_CIRCLE = new CssResourceReference(Spin.class, "sk-circle.css");

    public static CssResourceReference SPINKIT_CUBE_GRID = new CssResourceReference(Spin.class, "sk-cube-grid.css");

    public static CssResourceReference SPINKIT_FADING_CIRCLE = new CssResourceReference(Spin.class, "sk-fading-circle.css");

    public static CssResourceReference SPINKIT_FOLDING_CUBE = new CssResourceReference(Spin.class, "sk-folding-cube.css");

    public static List<CssResourceReference> css = Arrays.asList(//
            SPIN_CSS_DEFAULT, //
            SPIN_CSS_BALL, //
            SPIN_CSS_BAR, //
            SPIN_CSS_PING_PONG, //
            SPIN_CSS_BORDER, //
            SPIN_CSS_BOUNCING, //
            SPIN_CSS_CLOCK, //
            SPIN_CSS_CURTAIN, //
            SPIN_CSS_DOULE, //
            SPIN_CSS_MUSIC, //
            SPIN_CSS_POKEBALL, //
            SPIN_CSS_CELL, //
            SPINKIT_SQUARE, //
            SPINKIT_CHASE, //
            SPINKIT_SPINNER, //
            SPINKIT_RECT, //
            SPINKIT_BOUNCE, //
            SPINKIT_CIRCLE, //
            SPINKIT_CUBE_GRID, //
            SPINKIT_FADING_CIRCLE, //
            SPINKIT_FOLDING_CUBE//
    );

    public static String show() {
        return SHOW;
    }

    public static String hide() {
        return HIDE;
    }

    public static int type(String type) {
        int i = types.indexOf(type);
        if (i == -1) throw new IllegalArgumentException(types + " - " + types);
        return i;
    }

    public static CssResourceReference css(String type) {
        return css.get(type(type));
    }

    public static String body(SpinType valueOf) {
        if (valueOf == SpinType.sk_chase) {
            return "<div class=\"sk-chase-dot\"></div><div class=\"sk-chase-dot\"></div><div class=\"sk-chase-dot\"></div><div class=\"sk-chase-dot\"></div><div class=\"sk-chase-dot\"></div><div class=\"sk-chase-dot\"></div>";
        } else if (valueOf == SpinType.sk_spinner) {
            return "<div class=\"double-bounce1\"></div><div class=\"double-bounce2\"></div>";
        } else if (valueOf == SpinType.sk_rect) {
            return "<div class=\"rect1\"></div><div class=\"rect2\"></div><div class=\"rect3\"></div><div class=\"rect4\"></div><div class=\"rect5\"></div>";
        } else if (valueOf == SpinType.sk_bounce) {
            return "<div class=\"bounce1\"></div><div class=\"bounce2\"></div><div class=\"bounce3\"></div>";
        } else if (valueOf == SpinType.sk_circle) {
            return "<div class=\"sk-circle1 sk-child\"></div><div class=\"sk-circle2 sk-child\"></div><div class=\"sk-circle3 sk-child\"></div><div class=\"sk-circle4 sk-child\"></div><div class=\"sk-circle5 sk-child\"></div><div class=\"sk-circle6 sk-child\"></div><div class=\"sk-circle7 sk-child\"></div><div class=\"sk-circle8 sk-child\"></div><div class=\"sk-circle9 sk-child\"></div><div class=\"sk-circle10 sk-child\"></div><div class=\"sk-circle11 sk-child\"></div><div class=\"sk-circle12 sk-child\"></div>";
        } else if (valueOf == SpinType.sk_cube_grid) {
            return "<div class=\"sk-cube sk-cube1\"></div><div class=\"sk-cube sk-cube2\"></div><div class=\"sk-cube sk-cube3\"></div><div class=\"sk-cube sk-cube4\"></div><div class=\"sk-cube sk-cube5\"></div><div class=\"sk-cube sk-cube6\"></div><div class=\"sk-cube sk-cube7\"></div><div class=\"sk-cube sk-cube8\"></div><div class=\"sk-cube sk-cube9\"></div>";
        } else if (valueOf == SpinType.sk_fading_circle) {
            return "<div class=\"sk-circle1 sk-circle\"></div><div class=\"sk-circle2 sk-circle\"></div><div class=\"sk-circle3 sk-circle\"></div><div class=\"sk-circle4 sk-circle\"></div><div class=\"sk-circle5 sk-circle\"></div><div class=\"sk-circle6 sk-circle\"></div><div class=\"sk-circle7 sk-circle\"></div><div class=\"sk-circle8 sk-circle\"></div><div class=\"sk-circle9 sk-circle\"></div><div class=\"sk-circle10 sk-circle\"></div><div class=\"sk-circle11 sk-circle\"></div><div class=\"sk-circle12 sk-circle\"></div>";
        } else if (valueOf == SpinType.sk_folding_cube) {
            return "<div class=\"sk-cube1 sk-cube\"></div><div class=\"sk-cube2 sk-cube\"></div><div class=\"sk-cube4 sk-cube\"></div><div class=\"sk-cube3 sk-cube\"></div>";
        }
        return "";
    }
}
