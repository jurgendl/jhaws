package org.tools.hqlbuilder.webservice.jquery.ui.weloveicons;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.tools.hqlbuilder.webservice.wicket.CssResourceReference;

public class WeLoveIcons {
    public static CssResourceReference WE_LOVE_ICONS_CSS = new CssResourceReference(WeLoveIcons.class, "weloveiconfonts.css");

    public static CssResourceReference WE_LOVE_ICONS_SOCIAL_CSS = new CssResourceReference(WeLoveIcons.class, "weloveiconfonts-social.css")
            .addCssResourceReferenceDependency(WE_LOVE_ICONS_CSS);

    public static CssResourceReference SOCIAL_COLORS_CSS = new CssResourceReference(WeLoveIcons.class, "socialcolors.css");

    public static CssResourceReference SOCIAL_COLORS_HOVER_CSS = new CssResourceReference(WeLoveIcons.class, "socialcolorshover.css");

    public static void main(String[] args) {
        try {
            InputStream in = new FileInputStream("We Love Icon Fonts.htm");
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024 * 8];
            int read;
            while ((read = in.read(buffer)) > 0) {
                out.write(buffer, 0, read);
            }
            in.close();
            out.close();
            String html = new String(out.toByteArray());
            String[] type = {
                    "brandico",
                    "entypo",
                    "fontawesome",
                    "fontelico",
                    "iconicfill",
                    "iconicstroke",
                    "maki",
                    "openwebicons",
                    "typicons",
                    "zocial" };
            for (String t : type) {
                System.out.println("public static enum " + Character.toUpperCase(t.charAt(0)) + t.substring(1) + " {");
                Matcher matcher = Pattern.compile("class=\"" + t + "-[^\"]+\"").matcher(html);
                List<String> done = new ArrayList<String>();
                while (matcher.find()) {
                    String group = matcher.group().replaceAll("\"", "").replaceAll("class=", "");
                    if (done.contains(group)) {
                        continue;
                    }
                    String code = group.replaceAll(t + "-", "").replaceAll("-", "_").replaceAll(" ", "_");
                    System.out.println((done.size() > 0 ? "," : "") + code + "(\"" + group + "\") //");
                    done.add(group);
                }
                System.out.println(";");
                System.out.println("private final String code;");
                System.out.println(Character.toUpperCase(t.charAt(0)) + t.substring(1) + "(String code) { this.code = code; }");
                System.out.println("public String getCode() { return this.code; }");
                System.out.println("}");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static interface WLIFIcons {
        public String getCode();
    }

    public static enum WLIFont {
        brandico(Brandico.values()), //
        entypo(Entypo.values()), //
        fontawesome(Fontawesome.values()), //
        fontelico(Fontelico.values()), //
        iconicfill(Iconicfill.values()), //
        iconicstroke(Iconicstroke.values()), //
        maki(Maki.values()), //
        openwebicons(Openwebicons.values()), //
        typicons(Typicons.values()), //
        zocial(Zocial.values());//

        final WLIFIcons[] icons;

        WLIFont(WLIFIcons[] icons) {
            this.icons = icons;
        }

        public WLIFIcons[] getIcons() {
            return this.icons;
        }
    }

    public static enum Brandico implements WLIFIcons {
        blogger("brandico-blogger") //
        , blogger_rect("brandico-blogger-rect") //
        , deviantart("brandico-deviantart") //
        , facebook("brandico-facebook") //
        , facebook_rect("brandico-facebook-rect") //
        , friendfeed("brandico-friendfeed") //
        , friendfeed_rect("brandico-friendfeed-rect") //
        , github("brandico-github") //
        , github_text("brandico-github-text") //
        , googleplus_rect("brandico-googleplus-rect") //
        , icq("brandico-icq") //
        , instagram("brandico-instagram") //
        , instagram_filled("brandico-instagram-filled") //
        , jabber("brandico-jabber") //
        , lastfm_active("brandico-lastfm active") //
        , lastfm_rect("brandico-lastfm-rect") //
        , linkedin("brandico-linkedin") //
        , linkedin_rect("brandico-linkedin-rect") //
        , odnoklassniki("brandico-odnoklassniki") //
        , odnoklassniki_rect("brandico-odnoklassniki-rect") //
        , picasa("brandico-picasa") //
        , skype("brandico-skype") //
        , tumblr("brandico-tumblr") //
        , tumblr_rect("brandico-tumblr-rect") //
        , twitter("brandico-twitter") //
        , twitter_bird("brandico-twitter-bird") //
        , vimeo("brandico-vimeo") //
        , vimeo_rect("brandico-vimeo-rect") //
        , vkontakte_rect("brandico-vkontakte-rect") //
        , wordpress("brandico-wordpress") //
        , yandex("brandico-yandex") //
        , yandex_rect("brandico-yandex-rect") //
        ;
        private final String code;

        Brandico(String code) {
            this.code = code;
        }

        @Override
        public String getCode() {
            return this.code;
        }
    }

    public static enum Entypo implements WLIFIcons {
        address("entypo-address") //
        , adjust("entypo-adjust") //
        , air("entypo-air") //
        , alert("entypo-alert") //
        , archive("entypo-archive") //
        , arrow_combo("entypo-arrow-combo") //
        , arrows_ccw("entypo-arrows-ccw") //
        , attach("entypo-attach") //
        , attention("entypo-attention") //
        , back("entypo-back") //
        , back_in_time("entypo-back-in-time") //
        , bag("entypo-bag") //
        , basket("entypo-basket") //
        , battery("entypo-battery") //
        , behance("entypo-behance") //
        , bell("entypo-bell") //
        , block("entypo-block") //
        , book("entypo-book") //
        , book_open("entypo-book-open") //
        , bookmark("entypo-bookmark") //
        , bookmarks("entypo-bookmarks") //
        , box("entypo-box") //
        , briefcase("entypo-briefcase") //
        , brush("entypo-brush") //
        , bucket("entypo-bucket") //
        , calendar("entypo-calendar") //
        , camera("entypo-camera") //
        , cancel("entypo-cancel") //
        , cancel_circled("entypo-cancel-circled") //
        , cancel_squared("entypo-cancel-squared") //
        , cc("entypo-cc") //
        , cc_by("entypo-cc-by") //
        , cc_nc("entypo-cc-nc") //
        , cc_nc_eu("entypo-cc-nc-eu") //
        , cc_nc_jp("entypo-cc-nc-jp") //
        , cc_nd("entypo-cc-nd") //
        , cc_pd("entypo-cc-pd") //
        , cc_remix("entypo-cc-remix") //
        , cc_sa("entypo-cc-sa") //
        , cc_share("entypo-cc-share") //
        , cc_zero("entypo-cc-zero") //
        , ccw("entypo-ccw") //
        , cd("entypo-cd") //
        , chart_area("entypo-chart-area") //
        , chart_bar("entypo-chart-bar") //
        , chart_line("entypo-chart-line") //
        , chart_pie("entypo-chart-pie") //
        , chat("entypo-chat") //
        , check("entypo-check") //
        , clipboard("entypo-clipboard") //
        , clock("entypo-clock") //
        , cloud("entypo-cloud") //
        , cloud_thunder("entypo-cloud-thunder") //
        , code0("entypo-code") //
        , cog("entypo-cog") //
        , comment("entypo-comment") //
        , compass("entypo-compass") //
        , credit_card("entypo-credit-card") //
        , cup("entypo-cup") //
        , cw("entypo-cw") //
        , database("entypo-database") //
        , db_shape("entypo-db-shape") //
        , direction("entypo-direction") //
        , doc("entypo-doc") //
        , doc_landscape("entypo-doc-landscape") //
        , doc_text("entypo-doc-text") //
        , doc_text_inv("entypo-doc-text-inv") //
        , docs("entypo-docs") //
        , dot("entypo-dot") //
        , dot_2("entypo-dot-2") //
        , dot_3("entypo-dot-3") //
        , down("entypo-down") //
        , down_bold("entypo-down-bold") //
        , down_circled("entypo-down-circled") //
        , down_dir("entypo-down-dir") //
        , down_open("entypo-down-open") //
        , down_open_big("entypo-down-open-big") //
        , down_open_mini("entypo-down-open-mini") //
        , down_thin("entypo-down-thin") //
        , download("entypo-download") //
        , dribbble("entypo-dribbble") //
        , dribbble_circled("entypo-dribbble-circled") //
        , drive("entypo-drive") //
        , dropbox("entypo-dropbox") //
        , droplet("entypo-droplet") //
        , erase("entypo-erase") //
        , evernote("entypo-evernote") //
        , export("entypo-export") //
        , eye("entypo-eye") //
        , facebook("entypo-facebook") //
        , facebook_circled("entypo-facebook-circled") //
        , facebook_squared("entypo-facebook-squared") //
        , fast_backward("entypo-fast-backward") //
        , fast_forward("entypo-fast-forward") //
        , feather("entypo-feather") //
        , flag("entypo-flag") //
        , flash("entypo-flash") //
        , flashlight("entypo-flashlight") //
        , flattr("entypo-flattr") //
        , flickr("entypo-flickr") //
        , flickr_circled("entypo-flickr-circled") //
        , flight("entypo-flight") //
        , floppy("entypo-floppy") //
        , flow_branch("entypo-flow-branch") //
        , flow_cascade("entypo-flow-cascade") //
        , flow_line("entypo-flow-line") //
        , flow_parallel("entypo-flow-parallel") //
        , flow_tree("entypo-flow-tree") //
        , folder("entypo-folder") //
        , forward("entypo-forward") //
        , gauge("entypo-gauge") //
        , github("entypo-github") //
        , github_circled("entypo-github-circled") //
        , globe("entypo-globe") //
        , google_circles("entypo-google-circles") //
        , gplus("entypo-gplus") //
        , gplus_circled("entypo-gplus-circled") //
        , graduation_cap("entypo-graduation-cap") //
        , heart("entypo-heart") //
        , heart_empty("entypo-heart-empty") //
        , help("entypo-help") //
        , help_circled("entypo-help-circled") //
        , home("entypo-home") //
        , hourglass("entypo-hourglass") //
        , inbox("entypo-inbox") //
        , infinity("entypo-infinity") //
        , info("entypo-info") //
        , info_circled("entypo-info-circled") //
        , instagrem("entypo-instagrem") //
        , install("entypo-install") //
        , key("entypo-key") //
        , keyboard("entypo-keyboard") //
        , lamp("entypo-lamp") //
        , language("entypo-language") //
        , lastfm("entypo-lastfm") //
        , lastfm_circled("entypo-lastfm-circled") //
        , layout("entypo-layout") //
        , leaf("entypo-leaf") //
        , left("entypo-left") //
        , left_bold("entypo-left-bold") //
        , left_circled("entypo-left-circled") //
        , left_dir("entypo-left-dir") //
        , left_open("entypo-left-open") //
        , left_open_big("entypo-left-open-big") //
        , left_open_mini("entypo-left-open-mini") //
        , left_thin("entypo-left-thin") //
        , level_down("entypo-level-down") //
        , level_up("entypo-level-up") //
        , lifebuoy("entypo-lifebuoy") //
        , light_down("entypo-light-down") //
        , light_up("entypo-light-up") //
        , link("entypo-link") //
        , linkedin("entypo-linkedin") //
        , linkedin_circled("entypo-linkedin-circled") //
        , list("entypo-list") //
        , list_add("entypo-list-add") //
        , location("entypo-location") //
        , lock("entypo-lock") //
        , lock_open("entypo-lock-open") //
        , login("entypo-login") //
        , logo_db("entypo-logo-db") //
        , logout("entypo-logout") //
        , loop("entypo-loop") //
        , magnet("entypo-magnet") //
        , mail("entypo-mail") //
        , map("entypo-map") //
        , megaphone("entypo-megaphone") //
        , menu("entypo-menu") //
        , mic("entypo-mic") //
        , minus("entypo-minus") //
        , minus_circled("entypo-minus-circled") //
        , minus_squared("entypo-minus-squared") //
        , mixi("entypo-mixi") //
        , mobile("entypo-mobile") //
        , monitor("entypo-monitor") //
        , moon("entypo-moon") //
        , mouse("entypo-mouse") //
        , music("entypo-music") //
        , mute("entypo-mute") //
        , network("entypo-network") //
        , newspaper("entypo-newspaper") //
        , note("entypo-note") //
        , note_beamed("entypo-note-beamed") //
        , palette("entypo-palette") //
        , paper_plane("entypo-paper-plane") //
        , pause("entypo-pause") //
        , paypal("entypo-paypal") //
        , pencil("entypo-pencil") //
        , phone("entypo-phone") //
        , picasa("entypo-picasa") //
        , picture("entypo-picture") //
        , pinterest("entypo-pinterest") //
        , pinterest_circled("entypo-pinterest-circled") //
        , play("entypo-play") //
        , plus("entypo-plus") //
        , plus_circled("entypo-plus-circled") //
        , plus_squared("entypo-plus-squared") //
        , popup("entypo-popup") //
        , print("entypo-print") //
        , progress_0("entypo-progress-0") //
        , progress_1("entypo-progress-1") //
        , progress_2("entypo-progress-2") //
        , progress_3("entypo-progress-3") //
        , publish("entypo-publish") //
        , qq("entypo-qq") //
        , quote("entypo-quote") //
        , rdio("entypo-rdio") //
        , rdio_circled("entypo-rdio-circled") //
        , record("entypo-record") //
        , renren("entypo-renren") //
        , reply("entypo-reply") //
        , reply_all("entypo-reply-all") //
        , resize_full("entypo-resize-full") //
        , resize_small("entypo-resize-small") //
        , retweet("entypo-retweet") //
        , right("entypo-right") //
        , right_bold("entypo-right-bold") //
        , right_circled("entypo-right-circled") //
        , right_dir("entypo-right-dir") //
        , right_open("entypo-right-open") //
        , right_open_big("entypo-right-open-big") //
        , right_open_mini("entypo-right-open-mini") //
        , right_thin("entypo-right-thin") //
        , rocket("entypo-rocket") //
        , rss("entypo-rss") //
        , search("entypo-search") //
        , share("entypo-share") //
        , shareable("entypo-shareable") //
        , shuffle("entypo-shuffle") //
        , signal("entypo-signal") //
        , sina_weibo("entypo-sina-weibo") //
        , skype("entypo-skype") //
        , skype_circled("entypo-skype-circled") //
        , smashing("entypo-smashing") //
        , sound("entypo-sound") //
        , soundcloud("entypo-soundcloud") //
        , spotify("entypo-spotify") //
        , spotify_circled("entypo-spotify-circled") //
        , star("entypo-star") //
        , star_empty("entypo-star-empty") //
        , stop("entypo-stop") //
        , stumbleupon("entypo-stumbleupon") //
        , stumbleupon_circled("entypo-stumbleupon-circled") //
        , suitcase("entypo-suitcase") //
        , sweden("entypo-sweden") //
        , switch0("entypo-switch") //
        , tag("entypo-tag") //
        , tape("entypo-tape") //
        , target("entypo-target") //
        , thermometer("entypo-thermometer") //
        , thumbs_down("entypo-thumbs-down") //
        , thumbs_up("entypo-thumbs-up") //
        , ticket("entypo-ticket") //
        , to_end("entypo-to-end") //
        , to_start("entypo-to-start") //
        , tools("entypo-tools") //
        , traffic_cone("entypo-traffic-cone") //
        , trash("entypo-trash") //
        , trophy("entypo-trophy") //
        , tumblr("entypo-tumblr") //
        , tumblr_circled("entypo-tumblr-circled") //
        , twitter("entypo-twitter") //
        , twitter_circled("entypo-twitter-circled") //
        , up("entypo-up") //
        , up_bold("entypo-up-bold") //
        , up_circled("entypo-up-circled") //
        , up_dir("entypo-up-dir") //
        , up_open("entypo-up-open") //
        , up_open_big("entypo-up-open-big") //
        , up_open_mini("entypo-up-open-mini") //
        , up_thin("entypo-up-thin") //
        , upload("entypo-upload") //
        , upload_cloud("entypo-upload-cloud") //
        , user("entypo-user") //
        , user_add("entypo-user-add") //
        , users("entypo-users") //
        , vcard("entypo-vcard") //
        , video("entypo-video") //
        , vimeo("entypo-vimeo") //
        , vimeo_circled("entypo-vimeo-circled") //
        , vkontakte("entypo-vkontakte") //
        , volume("entypo-volume") //
        , water("entypo-water") //
        , window("entypo-window") //
        , minus_squared_toggle("entypo-minus-squared toggle") //
        ;
        private final String code;

        Entypo(String code) {
            this.code = code;
        }

        @Override
        public String getCode() {
            return this.code;
        }
    }

    public static enum Fontawesome implements WLIFIcons {
        heart("fontawesome-heart") //
        , adjust("fontawesome-adjust") //
        , align_center("fontawesome-align-center") //
        , align_justify("fontawesome-align-justify") //
        , align_left("fontawesome-align-left") //
        , align_right("fontawesome-align-right") //
        , ambulance("fontawesome-ambulance") //
        , angle_down("fontawesome-angle-down") //
        , angle_left("fontawesome-angle-left") //
        , angle_right("fontawesome-angle-right") //
        , angle_up("fontawesome-angle-up") //
        , arrow_down("fontawesome-arrow-down") //
        , arrow_left("fontawesome-arrow-left") //
        , arrow_right("fontawesome-arrow-right") //
        , arrow_up("fontawesome-arrow-up") //
        , asterisk("fontawesome-asterisk") //
        , backward("fontawesome-backward") //
        , ban_circle("fontawesome-ban-circle") //
        , bar_chart("fontawesome-bar-chart") //
        , barcode("fontawesome-barcode") //
        , beaker("fontawesome-beaker") //
        , beer("fontawesome-beer") //
        , bell("fontawesome-bell") //
        , bell_alt("fontawesome-bell-alt") //
        , bold("fontawesome-bold") //
        , bolt("fontawesome-bolt") //
        , book("fontawesome-book") //
        , bookmark("fontawesome-bookmark") //
        , bookmark_empty("fontawesome-bookmark-empty") //
        , briefcase("fontawesome-briefcase") //
        , building("fontawesome-building") //
        , bullhorn("fontawesome-bullhorn") //
        , calendar("fontawesome-calendar") //
        , camera("fontawesome-camera") //
        , camera_retro("fontawesome-camera-retro") //
        , caret_down("fontawesome-caret-down") //
        , caret_left("fontawesome-caret-left") //
        , caret_right("fontawesome-caret-right") //
        , caret_up("fontawesome-caret-up") //
        , certificate("fontawesome-certificate") //
        , check("fontawesome-check") //
        , check_empty("fontawesome-check-empty") //
        , chevron_down("fontawesome-chevron-down") //
        , chevron_left("fontawesome-chevron-left") //
        , chevron_right("fontawesome-chevron-right") //
        , chevron_up("fontawesome-chevron-up") //
        , circle("fontawesome-circle") //
        , circle_arrow_down("fontawesome-circle-arrow-down") //
        , circle_arrow_left("fontawesome-circle-arrow-left") //
        , circle_arrow_right("fontawesome-circle-arrow-right") //
        , circle_arrow_up("fontawesome-circle-arrow-up") //
        , circle_blank("fontawesome-circle-blank") //
        , cloud("fontawesome-cloud") //
        , cloud_download("fontawesome-cloud-download") //
        , cloud_upload("fontawesome-cloud-upload") //
        , coffee("fontawesome-coffee") //
        , cog("fontawesome-cog") //
        , cogs("fontawesome-cogs") //
        , columns("fontawesome-columns") //
        , comment("fontawesome-comment") //
        , comment_alt("fontawesome-comment-alt") //
        , comments("fontawesome-comments") //
        , comments_alt("fontawesome-comments-alt") //
        , copy("fontawesome-copy") //
        , credit_card("fontawesome-credit-card") //
        , cut("fontawesome-cut") //
        , dashboard("fontawesome-dashboard") //
        , desktop("fontawesome-desktop") //
        , double_angle_down("fontawesome-double-angle-down") //
        , double_angle_left("fontawesome-double-angle-left") //
        , double_angle_right("fontawesome-double-angle-right") //
        , double_angle_up("fontawesome-double-angle-up") //
        , download("fontawesome-download") //
        , download_alt("fontawesome-download-alt") //
        , edit("fontawesome-edit") //
        , eject("fontawesome-eject") //
        , envelope("fontawesome-envelope") //
        , envelope_alt("fontawesome-envelope-alt") //
        , exchange("fontawesome-exchange") //
        , exclamation_sign("fontawesome-exclamation-sign") //
        , external_link("fontawesome-external-link") //
        , eye_close("fontawesome-eye-close") //
        , eye_open("fontawesome-eye-open") //
        , facebook("fontawesome-facebook") //
        , facebook_sign("fontawesome-facebook-sign") //
        , facetime_video("fontawesome-facetime-video") //
        , fast_backward("fontawesome-fast-backward") //
        , fast_forward("fontawesome-fast-forward") //
        , fighter_jet("fontawesome-fighter-jet") //
        , file("fontawesome-file") //
        , file_alt("fontawesome-file-alt") //
        , film("fontawesome-film") //
        , filter("fontawesome-filter") //
        , fire("fontawesome-fire") //
        , flag("fontawesome-flag") //
        , folder_close("fontawesome-folder-close") //
        , folder_close_alt("fontawesome-folder-close-alt") //
        , folder_open("fontawesome-folder-open") //
        , folder_open_alt("fontawesome-folder-open-alt") //
        , font("fontawesome-font") //
        , food("fontawesome-food") //
        , forward("fontawesome-forward") //
        , fullscreen("fontawesome-fullscreen") //
        , gift("fontawesome-gift") //
        , github("fontawesome-github") //
        , github_alt("fontawesome-github-alt") //
        , github_sign("fontawesome-github-sign") //
        , glass("fontawesome-glass") //
        , globe("fontawesome-globe") //
        , google_plus("fontawesome-google-plus") //
        , google_plus_sign("fontawesome-google-plus-sign") //
        , group("fontawesome-group") //
        , h_sign("fontawesome-h-sign") //
        , hand_down("fontawesome-hand-down") //
        , hand_left("fontawesome-hand-left") //
        , hand_right("fontawesome-hand-right") //
        , hand_up("fontawesome-hand-up") //
        , hdd("fontawesome-hdd") //
        , headphones("fontawesome-headphones") //
        , heart_empty("fontawesome-heart-empty") //
        , home("fontawesome-home") //
        , hospital("fontawesome-hospital") //
        , inbox("fontawesome-inbox") //
        , indent_left("fontawesome-indent-left") //
        , indent_right("fontawesome-indent-right") //
        , info_sign("fontawesome-info-sign") //
        , italic("fontawesome-italic") //
        , key("fontawesome-key") //
        , laptop("fontawesome-laptop") //
        , leaf("fontawesome-leaf") //
        , legal("fontawesome-legal") //
        , lemon("fontawesome-lemon") //
        , lightbulb("fontawesome-lightbulb") //
        , link("fontawesome-link") //
        , linkedin("fontawesome-linkedin") //
        , linkedin_sign("fontawesome-linkedin-sign") //
        , list("fontawesome-list") //
        , list_alt("fontawesome-list-alt") //
        , list_ol("fontawesome-list-ol") //
        , list_ul("fontawesome-list-ul") //
        , lock("fontawesome-lock") //
        , magic("fontawesome-magic") //
        , magnet("fontawesome-magnet") //
        , map_marker("fontawesome-map-marker") //
        , medkit("fontawesome-medkit") //
        , minus("fontawesome-minus") //
        , minus_sign("fontawesome-minus-sign") //
        , mobile_phone("fontawesome-mobile-phone") //
        , money("fontawesome-money") //
        , move("fontawesome-move") //
        , music("fontawesome-music") //
        , off("fontawesome-off") //
        , ok("fontawesome-ok") //
        , ok_circle("fontawesome-ok-circle") //
        , ok_sign("fontawesome-ok-sign") //
        , paper_clip("fontawesome-paper-clip") //
        , paste("fontawesome-paste") //
        , pause("fontawesome-pause") //
        , pencil("fontawesome-pencil") //
        , phone("fontawesome-phone") //
        , phone_sign("fontawesome-phone-sign") //
        , picture("fontawesome-picture") //
        , pinterest("fontawesome-pinterest") //
        , pinterest_sign("fontawesome-pinterest-sign") //
        , plane("fontawesome-plane") //
        , play("fontawesome-play") //
        , play_circle("fontawesome-play-circle") //
        , plus("fontawesome-plus") //
        , plus_sign("fontawesome-plus-sign") //
        , plus_sign_alt("fontawesome-plus-sign-alt") //
        , print("fontawesome-print") //
        , pushpin("fontawesome-pushpin") //
        , qrcode("fontawesome-qrcode") //
        , question_sign("fontawesome-question-sign") //
        , quote_left("fontawesome-quote-left") //
        , quote_right("fontawesome-quote-right") //
        , random("fontawesome-random") //
        , refresh("fontawesome-refresh") //
        , remove("fontawesome-remove") //
        , remove_circle("fontawesome-remove-circle") //
        , remove_sign("fontawesome-remove-sign") //
        , reorder("fontawesome-reorder") //
        , repeat("fontawesome-repeat") //
        , reply("fontawesome-reply") //
        , resize_full("fontawesome-resize-full") //
        , resize_horizontal("fontawesome-resize-horizontal") //
        , resize_small("fontawesome-resize-small") //
        , resize_vertical("fontawesome-resize-vertical") //
        , retweet("fontawesome-retweet") //
        , road("fontawesome-road") //
        , rss("fontawesome-rss") //
        , save("fontawesome-save") //
        , screenshot("fontawesome-screenshot") //
        , search("fontawesome-search") //
        , share("fontawesome-share") //
        , share_alt("fontawesome-share-alt") //
        , shopping_cart("fontawesome-shopping-cart") //
        , sign_blank("fontawesome-sign-blank") //
        , signal("fontawesome-signal") //
        , signin("fontawesome-signin") //
        , signout("fontawesome-signout") //
        , sitemap("fontawesome-sitemap") //
        , sort("fontawesome-sort") //
        , sort_down("fontawesome-sort-down") //
        , sort_up("fontawesome-sort-up") //
        , spinner("fontawesome-spinner") //
        , star("fontawesome-star") //
        , star_empty("fontawesome-star-empty") //
        , star_half("fontawesome-star-half") //
        , step_backward("fontawesome-step-backward") //
        , step_forward("fontawesome-step-forward") //
        , stethoscope("fontawesome-stethoscope") //
        , stop("fontawesome-stop") //
        , strikethrough("fontawesome-strikethrough") //
        , suitcase("fontawesome-suitcase") //
        , table("fontawesome-table") //
        , tablet("fontawesome-tablet") //
        , tag("fontawesome-tag") //
        , tags("fontawesome-tags") //
        , tasks("fontawesome-tasks") //
        , text_height("fontawesome-text-height") //
        , text_width("fontawesome-text-width") //
        , th("fontawesome-th") //
        , th_large("fontawesome-th-large") //
        , th_list("fontawesome-th-list") //
        , thumbs_down("fontawesome-thumbs-down") //
        , thumbs_up("fontawesome-thumbs-up") //
        , time("fontawesome-time") //
        , tint("fontawesome-tint") //
        , trash("fontawesome-trash") //
        , trophy("fontawesome-trophy") //
        , truck("fontawesome-truck") //
        , twitter("fontawesome-twitter") //
        , twitter_sign("fontawesome-twitter-sign") //
        , umbrella("fontawesome-umbrella") //
        , underline("fontawesome-underline") //
        , undo("fontawesome-undo") //
        , unlock("fontawesome-unlock") //
        , upload("fontawesome-upload") //
        , upload_alt("fontawesome-upload-alt") //
        , user("fontawesome-user") //
        , user_md("fontawesome-user-md") //
        , volume_down("fontawesome-volume-down") //
        , volume_off("fontawesome-volume-off") //
        , volume_up("fontawesome-volume-up") //
        , warning_sign("fontawesome-warning-sign") //
        , wrench("fontawesome-wrench") //
        , zoom_in("fontawesome-zoom-in") //
        , zoom_out("fontawesome-zoom-out") //
        ;
        private final String code;

        Fontawesome(String code) {
            this.code = code;
        }

        @Override
        public String getCode() {
            return this.code;
        }
    }

    public static enum Fontelico implements WLIFIcons {
        chrome("fontelico-chrome") //
        , emo_angry("fontelico-emo-angry") //
        , emo_beer("fontelico-emo-beer") //
        , emo_coffee("fontelico-emo-coffee") //
        , emo_cry("fontelico-emo-cry") //
        , emo_devil("fontelico-emo-devil") //
        , emo_displeased("fontelico-emo-displeased") //
        , emo_grin("fontelico-emo-grin") //
        , emo_happy("fontelico-emo-happy") //
        , emo_laugh("fontelico-emo-laugh") //
        , emo_saint("fontelico-emo-saint") //
        , emo_shoot("fontelico-emo-shoot") //
        , emo_sleep("fontelico-emo-sleep") //
        , emo_squint("fontelico-emo-squint") //
        , emo_sunglasses("fontelico-emo-sunglasses") //
        , emo_surprised("fontelico-emo-surprised") //
        , emo_thumbsup("fontelico-emo-thumbsup") //
        , emo_tongue("fontelico-emo-tongue") //
        , emo_unhappy("fontelico-emo-unhappy") //
        , emo_wink("fontelico-emo-wink") //
        , emo_wink2("fontelico-emo-wink2") //
        , firefox("fontelico-firefox") //
        , ie("fontelico-ie") //
        , opera("fontelico-opera") //
        , spin1("fontelico-spin1") //
        , spin2("fontelico-spin2") //
        , spin3("fontelico-spin3") //
        , spin4("fontelico-spin4") //
        , spin5("fontelico-spin5") //
        , spin6("fontelico-spin6") //
        ;
        private final String code;

        Fontelico(String code) {
            this.code = code;
        }

        @Override
        public String getCode() {
            return this.code;
        }
    }

    public static enum Iconicfill implements WLIFIcons {
        aperture("iconicfill-aperture") //
        , aperture_alt("iconicfill-aperture-alt") //
        , arrow_down("iconicfill-arrow-down") //
        , arrow_down_alt1("iconicfill-arrow-down-alt1") //
        , arrow_down_alt2("iconicfill-arrow-down-alt2") //
        , arrow_left("iconicfill-arrow-left") //
        , arrow_left_alt1("iconicfill-arrow-left-alt1") //
        , arrow_left_alt2("iconicfill-arrow-left-alt2") //
        , arrow_right("iconicfill-arrow-right") //
        , arrow_right_alt1("iconicfill-arrow-right-alt1") //
        , arrow_right_alt2("iconicfill-arrow-right-alt2") //
        , arrow_up("iconicfill-arrow-up") //
        , arrow_up_alt1("iconicfill-arrow-up-alt1") //
        , arrow_up_alt2("iconicfill-arrow-up-alt2") //
        , article("iconicfill-article") //
        , at("iconicfill-at") //
        , award_fill("iconicfill-award-fill") //
        , bars("iconicfill-bars") //
        , bars_alt("iconicfill-bars-alt") //
        , battery_charging("iconicfill-battery-charging") //
        , battery_empty("iconicfill-battery-empty") //
        , battery_full("iconicfill-battery-full") //
        , battery_half("iconicfill-battery-half") //
        , beaker("iconicfill-beaker") //
        , beaker_alt("iconicfill-beaker-alt") //
        , bolt("iconicfill-bolt") //
        , book("iconicfill-book") //
        , book_alt("iconicfill-book-alt") //
        , book_alt2("iconicfill-book-alt2") //
        , box("iconicfill-box") //
        , brush("iconicfill-brush") //
        , brush_alt("iconicfill-brush-alt") //
        , calendar("iconicfill-calendar") //
        , calendar_alt_fill("iconicfill-calendar-alt-fill") //
        , camera("iconicfill-camera") //
        , cd("iconicfill-cd") //
        , chart("iconicfill-chart") //
        , chart_alt("iconicfill-chart-alt") //
        , chat("iconicfill-chat") //
        , chat_alt_fill("iconicfill-chat-alt-fill") //
        , check("iconicfill-check") //
        , check_alt("iconicfill-check-alt") //
        , clock("iconicfill-clock") //
        , cloud("iconicfill-cloud") //
        , cloud_download("iconicfill-cloud-download") //
        , cloud_upload("iconicfill-cloud-upload") //
        , cog("iconicfill-cog") //
        , comment_alt1_fill("iconicfill-comment-alt1-fill") //
        , comment_alt2_fill("iconicfill-comment-alt2-fill") //
        , comment_fill("iconicfill-comment-fill") //
        , compass("iconicfill-compass") //
        , cursor("iconicfill-cursor") //
        , curved_arrow("iconicfill-curved-arrow") //
        , denied("iconicfill-denied") //
        , dial("iconicfill-dial") //
        , document_alt_fill("iconicfill-document-alt-fill") //
        , document_fill("iconicfill-document-fill") //
        , download("iconicfill-download") //
        , eject("iconicfill-eject") //
        , equalizer("iconicfill-equalizer") //
        , eye("iconicfill-eye") //
        , eyedropper("iconicfill-eyedropper") //
        , first("iconicfill-first") //
        , folder_fill("iconicfill-folder-fill") //
        , fork("iconicfill-fork") //
        , fullscreen("iconicfill-fullscreen") //
        , fullscreen_alt("iconicfill-fullscreen-alt") //
        , fullscreen_exit("iconicfill-fullscreen-exit") //
        , fullscreen_exit_alt("iconicfill-fullscreen-exit-alt") //
        , hash("iconicfill-hash") //
        , headphones("iconicfill-headphones") //
        , heart_fill("iconicfill-heart-fill") //
        , home("iconicfill-home") //
        , image("iconicfill-image") //
        , info("iconicfill-info") //
        , iphone("iconicfill-iphone") //
        , key_fill("iconicfill-key-fill") //
        , last("iconicfill-last") //
        , layers("iconicfill-layers") //
        , layers_alt("iconicfill-layers-alt") //
        , left_quote("iconicfill-left-quote") //
        , left_quote_alt("iconicfill-left-quote-alt") //
        , lightbulb("iconicfill-lightbulb") //
        , link("iconicfill-link") //
        , list("iconicfill-list") //
        , list_nested("iconicfill-list-nested") //
        , lock_fill("iconicfill-lock-fill") //
        , loop_alt1("iconicfill-loop-alt1") //
        , loop_alt2("iconicfill-loop-alt2") //
        , loop_alt3("iconicfill-loop-alt3") //
        , loop_alt4("iconicfill-loop-alt4") //
        , magnifying_glass("iconicfill-magnifying-glass") //
        , mail("iconicfill-mail") //
        , map_pin_alt("iconicfill-map-pin-alt") //
        , map_pin_fill("iconicfill-map-pin-fill") //
        , mic("iconicfill-mic") //
        , minus("iconicfill-minus") //
        , minus_alt("iconicfill-minus-alt") //
        , moon_fill("iconicfill-moon-fill") //
        , move("iconicfill-move") //
        , move_alt1("iconicfill-move-alt1") //
        , move_alt2("iconicfill-move-alt2") //
        , move_horizontal("iconicfill-move-horizontal") //
        , move_horizontal_alt1("iconicfill-move-horizontal-alt1") //
        , move_horizontal_alt2("iconicfill-move-horizontal-alt2") //
        , move_vertical("iconicfill-move-vertical") //
        , move_vertical_alt1("iconicfill-move-vertical-alt1") //
        , move_vertical_alt2("iconicfill-move-vertical-alt2") //
        , movie("iconicfill-movie") //
        , new_window("iconicfill-new-window") //
        , paperclip("iconicfill-paperclip") //
        , pause("iconicfill-pause") //
        , pen("iconicfill-pen") //
        , pen_alt_fill("iconicfill-pen-alt-fill") //
        , pen_alt2("iconicfill-pen-alt2") //
        , pilcrow("iconicfill-pilcrow") //
        , pin("iconicfill-pin") //
        , play("iconicfill-play") //
        , play_alt("iconicfill-play-alt") //
        , plus("iconicfill-plus") //
        , plus_alt("iconicfill-plus-alt") //
        , question_mark("iconicfill-question-mark") //
        , rain("iconicfill-rain") //
        , read_more("iconicfill-read-more") //
        , reload("iconicfill-reload") //
        , reload_alt("iconicfill-reload-alt") //
        , right_quote("iconicfill-right-quote") //
        , right_quote_alt("iconicfill-right-quote-alt") //
        , rss("iconicfill-rss") //
        , rss_alt("iconicfill-rss-alt") //
        , share("iconicfill-share") //
        , spin("iconicfill-spin") //
        , spin_alt("iconicfill-spin-alt") //
        , star("iconicfill-star") //
        , steering_wheel("iconicfill-steering-wheel") //
        , stop("iconicfill-stop") //
        , sun_fill("iconicfill-sun-fill") //
        , tag_fill("iconicfill-tag-fill") //
        , target("iconicfill-target") //
        , transfer("iconicfill-transfer") //
        , trash_fill("iconicfill-trash-fill") //
        , umbrella("iconicfill-umbrella") //
        , undo("iconicfill-undo") //
        , unlock_fill("iconicfill-unlock-fill") //
        , upload("iconicfill-upload") //
        , user("iconicfill-user") //
        , volume("iconicfill-volume") //
        , volume_mute("iconicfill-volume-mute") //
        , wrench("iconicfill-wrench") //
        , x("iconicfill-x") //
        , x_alt("iconicfill-x-alt") //
        ;
        private final String code;

        Iconicfill(String code) {
            this.code = code;
        }

        @Override
        public String getCode() {
            return this.code;
        }
    }

    public static enum Iconicstroke implements WLIFIcons {
        aperture("iconicstroke-aperture") //
        , aperture_alt("iconicstroke-aperture-alt") //
        , arrow_down("iconicstroke-arrow-down") //
        , arrow_down_alt1("iconicstroke-arrow-down-alt1") //
        , arrow_down_alt2("iconicstroke-arrow-down-alt2") //
        , arrow_left("iconicstroke-arrow-left") //
        , arrow_left_alt1("iconicstroke-arrow-left-alt1") //
        , arrow_left_alt2("iconicstroke-arrow-left-alt2") //
        , arrow_right("iconicstroke-arrow-right") //
        , arrow_right_alt1("iconicstroke-arrow-right-alt1") //
        , arrow_right_alt2("iconicstroke-arrow-right-alt2") //
        , arrow_up("iconicstroke-arrow-up") //
        , arrow_up_alt1("iconicstroke-arrow-up-alt1") //
        , arrow_up_alt2("iconicstroke-arrow-up-alt2") //
        , article("iconicstroke-article") //
        , at("iconicstroke-at") //
        , award_stroke("iconicstroke-award-stroke") //
        , bars("iconicstroke-bars") //
        , bars_alt("iconicstroke-bars-alt") //
        , battery_charging("iconicstroke-battery-charging") //
        , battery_empty("iconicstroke-battery-empty") //
        , battery_full("iconicstroke-battery-full") //
        , battery_half("iconicstroke-battery-half") //
        , beaker("iconicstroke-beaker") //
        , beaker_alt("iconicstroke-beaker-alt") //
        , bolt("iconicstroke-bolt") //
        , book("iconicstroke-book") //
        , book_alt("iconicstroke-book-alt") //
        , book_alt2("iconicstroke-book-alt2") //
        , box("iconicstroke-box") //
        , brush("iconicstroke-brush") //
        , brush_alt("iconicstroke-brush-alt") //
        , calendar("iconicstroke-calendar") //
        , calendar_alt_stroke("iconicstroke-calendar-alt-stroke") //
        , camera("iconicstroke-camera") //
        , cd("iconicstroke-cd") //
        , chart("iconicstroke-chart") //
        , chart_alt("iconicstroke-chart-alt") //
        , chat("iconicstroke-chat") //
        , chat_alt_stroke("iconicstroke-chat-alt-stroke") //
        , check("iconicstroke-check") //
        , check_alt("iconicstroke-check-alt") //
        , clock("iconicstroke-clock") //
        , cloud("iconicstroke-cloud") //
        , cloud_download("iconicstroke-cloud-download") //
        , cloud_upload("iconicstroke-cloud-upload") //
        , cog("iconicstroke-cog") //
        , comment_alt1_stroke("iconicstroke-comment-alt1-stroke") //
        , comment_alt2_stroke("iconicstroke-comment-alt2-stroke") //
        , comment_stroke("iconicstroke-comment-stroke") //
        , compass("iconicstroke-compass") //
        , cursor("iconicstroke-cursor") //
        , curved_arrow("iconicstroke-curved-arrow") //
        , denied("iconicstroke-denied") //
        , dial("iconicstroke-dial") //
        , document_alt_stroke("iconicstroke-document-alt-stroke") //
        , document_stroke("iconicstroke-document-stroke") //
        , download("iconicstroke-download") //
        , eject("iconicstroke-eject") //
        , equalizer("iconicstroke-equalizer") //
        , eye("iconicstroke-eye") //
        , eyedropper("iconicstroke-eyedropper") //
        , first("iconicstroke-first") //
        , folder_stroke("iconicstroke-folder-stroke") //
        , fork("iconicstroke-fork") //
        , fullscreen("iconicstroke-fullscreen") //
        , fullscreen_alt("iconicstroke-fullscreen-alt") //
        , fullscreen_exit("iconicstroke-fullscreen-exit") //
        , fullscreen_exit_alt("iconicstroke-fullscreen-exit-alt") //
        , hash("iconicstroke-hash") //
        , headphones("iconicstroke-headphones") //
        , heart_stroke("iconicstroke-heart-stroke") //
        , home("iconicstroke-home") //
        , image("iconicstroke-image") //
        , info("iconicstroke-info") //
        , iphone("iconicstroke-iphone") //
        , key_stroke("iconicstroke-key-stroke") //
        , last("iconicstroke-last") //
        , layers("iconicstroke-layers") //
        , layers_alt("iconicstroke-layers-alt") //
        , left_quote("iconicstroke-left-quote") //
        , left_quote_alt("iconicstroke-left-quote-alt") //
        , lightbulb("iconicstroke-lightbulb") //
        , link("iconicstroke-link") //
        , list("iconicstroke-list") //
        , list_nested("iconicstroke-list-nested") //
        , lock_stroke("iconicstroke-lock-stroke") //
        , loop_alt1("iconicstroke-loop-alt1") //
        , loop_alt2("iconicstroke-loop-alt2") //
        , loop_alt3("iconicstroke-loop-alt3") //
        , loop_alt4("iconicstroke-loop-alt4") //
        , magnifying_glass("iconicstroke-magnifying-glass") //
        , mail("iconicstroke-mail") //
        , map_pin_alt("iconicstroke-map-pin-alt") //
        , map_pin_stroke("iconicstroke-map-pin-stroke") //
        , mic("iconicstroke-mic") //
        , minus("iconicstroke-minus") //
        , minus_alt("iconicstroke-minus-alt") //
        , moon_stroke("iconicstroke-moon-stroke") //
        , move("iconicstroke-move") //
        , move_alt1("iconicstroke-move-alt1") //
        , move_alt2("iconicstroke-move-alt2") //
        , move_horizontal("iconicstroke-move-horizontal") //
        , move_horizontal_alt1("iconicstroke-move-horizontal-alt1") //
        , move_horizontal_alt2("iconicstroke-move-horizontal-alt2") //
        , move_vertical("iconicstroke-move-vertical") //
        , move_vertical_alt1("iconicstroke-move-vertical-alt1") //
        , move_vertical_alt2("iconicstroke-move-vertical-alt2") //
        , movie("iconicstroke-movie") //
        , new_window("iconicstroke-new-window") //
        , paperclip("iconicstroke-paperclip") //
        , pause("iconicstroke-pause") //
        , pen("iconicstroke-pen") //
        , pen_alt_stroke("iconicstroke-pen-alt-stroke") //
        , pen_alt2("iconicstroke-pen-alt2") //
        , pilcrow("iconicstroke-pilcrow") //
        , pin("iconicstroke-pin") //
        , play("iconicstroke-play") //
        , play_alt("iconicstroke-play-alt") //
        , plus("iconicstroke-plus") //
        , plus_alt("iconicstroke-plus-alt") //
        , question_mark("iconicstroke-question-mark") //
        , rain("iconicstroke-rain") //
        , read_more("iconicstroke-read-more") //
        , reload("iconicstroke-reload") //
        , reload_alt("iconicstroke-reload-alt") //
        , right_quote("iconicstroke-right-quote") //
        , right_quote_alt("iconicstroke-right-quote-alt") //
        , rss("iconicstroke-rss") //
        , rss_alt("iconicstroke-rss-alt") //
        , share("iconicstroke-share") //
        , spin("iconicstroke-spin") //
        , spin_alt("iconicstroke-spin-alt") //
        , star("iconicstroke-star") //
        , steering_wheel("iconicstroke-steering-wheel") //
        , stop("iconicstroke-stop") //
        , sun_stroke("iconicstroke-sun-stroke") //
        , tag_stroke("iconicstroke-tag-stroke") //
        , target("iconicstroke-target") //
        , transfer("iconicstroke-transfer") //
        , trash_stroke("iconicstroke-trash-stroke") //
        , umbrella("iconicstroke-umbrella") //
        , undo("iconicstroke-undo") //
        , unlock_stroke("iconicstroke-unlock-stroke") //
        , upload("iconicstroke-upload") //
        , user("iconicstroke-user") //
        , volume("iconicstroke-volume") //
        , volume_mute("iconicstroke-volume-mute") //
        , wrench("iconicstroke-wrench") //
        , x("iconicstroke-x") //
        , x_alt("iconicstroke-x-alt") //
        ;
        private final String code;

        Iconicstroke(String code) {
            this.code = code;
        }

        @Override
        public String getCode() {
            return this.code;
        }
    }

    public static enum Maki implements WLIFIcons {
        aboveground_rail("maki-aboveground-rail") //
        , airfield("maki-airfield") //
        , airport("maki-airport") //
        , art_gallery("maki-art-gallery") //
        , bar("maki-bar") //
        , baseball("maki-baseball") //
        , basketball("maki-basketball") //
        , beer("maki-beer") //
        , belowground_rail("maki-belowground-rail") //
        , bicycle("maki-bicycle") //
        , bus("maki-bus") //
        , cafe("maki-cafe") //
        , campsite("maki-campsite") //
        , cemetery("maki-cemetery") //
        , cinema("maki-cinema") //
        , college("maki-college") //
        , commerical_building("maki-commerical-building") //
        , credit_card("maki-credit-card") //
        , cricket("maki-cricket") //
        , embassy("maki-embassy") //
        , fast_food("maki-fast-food") //
        , ferry("maki-ferry") //
        , fire_station("maki-fire-station") //
        , football("maki-football") //
        , fuel("maki-fuel") //
        , garden("maki-garden") //
        , giraffe("maki-giraffe") //
        , golf("maki-golf") //
        , grocery_store("maki-grocery-store") //
        , harbor("maki-harbor") //
        , heliport("maki-heliport") //
        , hospital("maki-hospital") //
        , industrial_building("maki-industrial-building") //
        , library("maki-library") //
        , lodging("maki-lodging") //
        , london_underground("maki-london-underground") //
        , minefield("maki-minefield") //
        , monument("maki-monument") //
        , museum("maki-museum") //
        , pharmacy("maki-pharmacy") //
        , pitch("maki-pitch") //
        , police("maki-police") //
        , post("maki-post") //
        , prison("maki-prison") //
        , rail("maki-rail") //
        , religious_christian("maki-religious-christian") //
        , religious_islam("maki-religious-islam") //
        , religious_jewish("maki-religious-jewish") //
        , restaurant("maki-restaurant") //
        , roadblock("maki-roadblock") //
        , school("maki-school") //
        , shop("maki-shop") //
        , skiing("maki-skiing") //
        , soccer("maki-soccer") //
        , swimming("maki-swimming") //
        , tennis("maki-tennis") //
        , theatre("maki-theatre") //
        , toilet("maki-toilet") //
        , town_hall("maki-town-hall") //
        , trash("maki-trash") //
        , tree_1("maki-tree-1") //
        , tree_2("maki-tree-2") //
        , warehouse("maki-warehouse") //
        ;
        private final String code;

        Maki(String code) {
            this.code = code;
        }

        @Override
        public String getCode() {
            return this.code;
        }
    }

    public static enum Openwebicons implements WLIFIcons {
        activity("openwebicons-activity") //
        , apml("openwebicons-apml") //
        , browserid("openwebicons-browserid") //
        , cc("openwebicons-cc") //
        , cc_by("openwebicons-cc-by") //
        , cc_nc("openwebicons-cc-nc") //
        , cc_nc_eu("openwebicons-cc-nc-eu") //
        , cc_nc_jp("openwebicons-cc-nc-jp") //
        , cc_nd("openwebicons-cc-nd") //
        , cc_public("openwebicons-cc-public") //
        , cc_remix("openwebicons-cc-remix") //
        , cc_sa("openwebicons-cc-sa") //
        , cc_share("openwebicons-cc-share") //
        , cc_zero("openwebicons-cc-zero") //
        , connectivity("openwebicons-connectivity") //
        , css3("openwebicons-css3") //
        , dataportability("openwebicons-dataportability") //
        , epub("openwebicons-epub") //
        , federated("openwebicons-federated") //
        , feed("openwebicons-feed") //
        , feed_simple("openwebicons-feed-simple") //
        , foaf("openwebicons-foaf") //
        , geo("openwebicons-geo") //
        , html5("openwebicons-html5") //
        , info_card("openwebicons-info-card") //
        , markdown("openwebicons-markdown") //
        , microformats("openwebicons-microformats") //
        , oauth("openwebicons-oauth") //
        , odata("openwebicons-odata") //
        , open_share("openwebicons-open-share") //
        , open_share_simple("openwebicons-open-share-simple") //
        , open_web("openwebicons-open-web") //
        , opengraph("openwebicons-opengraph") //
        , openid("openwebicons-openid") //
        , opensearch("openwebicons-opensearch") //
        , opml("openwebicons-opml") //
        , ostatus("openwebicons-ostatus") //
        , ostatus_simple("openwebicons-ostatus-simple") //
        , persona("openwebicons-persona") //
        , qr("openwebicons-qr") //
        , rdf("openwebicons-rdf") //
        , remote_storage("openwebicons-remote-storage") //
        , semantics("openwebicons-semantics") //
        , share("openwebicons-share") //
        , share_simple("openwebicons-share-simple") //
        , tosdr("openwebicons-tosdr") //
        , web_intents("openwebicons-web-intents") //
        , xmpp("openwebicons-xmpp") //
        ;
        private final String code;

        Openwebicons(String code) {
            this.code = code;
        }

        @Override
        public String getCode() {
            return this.code;
        }
    }

    public static enum Typicons implements WLIFIcons {
        anchor("typicons-anchor") //
        , archive("typicons-archive") //
        , back("typicons-back") //
        , backspace("typicons-backspace") //
        , barChart("typicons-barChart") //
        , batteryHigh("typicons-batteryHigh") //
        , batteryLow("typicons-batteryLow") //
        , batteryMid("typicons-batteryMid") //
        , batteryNone("typicons-batteryNone") //
        , batteryPower("typicons-batteryPower") //
        , beta("typicons-beta") //
        , bookmark("typicons-bookmark") //
        , brightness("typicons-brightness") //
        , camera("typicons-camera") //
        , cancel("typicons-cancel") //
        , chat("typicons-chat") //
        , cog("typicons-cog") //
        , compass("typicons-compass") //
        , cut("typicons-cut") //
        , delete("typicons-delete") //
        , directions("typicons-directions") //
        , down("typicons-down") //
        , edit("typicons-edit") //
        , eject("typicons-eject") //
        , equals("typicons-equals") //
        , expand("typicons-expand") //
        , export("typicons-export") //
        , feed("typicons-feed") //
        , flag("typicons-flag") //
        , forward("typicons-forward") //
        , globe("typicons-globe") //
        , grid("typicons-grid") //
        , group("typicons-group") //
        , heart("typicons-heart") //
        , home("typicons-home") //
        , infinity("typicons-infinity") //
        , info("typicons-info") //
        , key("typicons-key") //
        , left("typicons-left") //
        , lineChart("typicons-lineChart") //
        , list("typicons-list") //
        , location("typicons-location") //
        , lock("typicons-lock") //
        , loop("typicons-loop") //
        , mail("typicons-mail") //
        , message("typicons-message") //
        , microphone("typicons-microphone") //
        , minus("typicons-minus") //
        , mobile("typicons-mobile") //
        , move("typicons-move") //
        , music("typicons-music") //
        , mute("typicons-mute") //
        , next("typicons-next") //
        , phone("typicons-phone") //
        , pieChart("typicons-pieChart") //
        , pin("typicons-pin") //
        , plus("typicons-plus") //
        , power("typicons-power") //
        , previous("typicons-previous") //
        , puzzle("typicons-puzzle") //
        , radar("typicons-radar") //
        , refresh("typicons-refresh") //
        , repeat("typicons-repeat") //
        , right("typicons-right") //
        , rss("typicons-rss") //
        , shuffle("typicons-shuffle") //
        , spanner("typicons-spanner") //
        , star("typicons-star") //
        , sync("typicons-sync") //
        , tab("typicons-tab") //
        , tag("typicons-tag") //
        , thumbsDown("typicons-thumbsDown") //
        , thumbsUp("typicons-thumbsUp") //
        , tick("typicons-tick") //
        , time("typicons-time") //
        , times("typicons-times") //
        , unknown("typicons-unknown") //
        , unlock("typicons-unlock") //
        , up("typicons-up") //
        , user("typicons-user") //
        , views("typicons-views") //
        , volume("typicons-volume") //
        , warning("typicons-warning") //
        , world("typicons-world") //
        , write("typicons-write") //
        , zoomIn("typicons-zoomIn") //
        , zoomOut("typicons-zoomOut") //
        ;
        private final String code;

        Typicons(String code) {
            this.code = code;
        }

        @Override
        public String getCode() {
            return this.code;
        }
    }

    public static enum Zocial implements WLIFIcons {
        acrobat("zocial-acrobat") //
        , amazon("zocial-amazon") //
        , android("zocial-android") //
        , angellist("zocial-angellist") //
        , aol("zocial-aol") //
        , appnet("zocial-appnet") //
        , appstore("zocial-appstore") //
        , bitbucket("zocial-bitbucket") //
        , bitcoin("zocial-bitcoin") //
        , blogger("zocial-blogger") //
        , buffer("zocial-buffer") //
        , cal("zocial-cal") //
        , call("zocial-call") //
        , cart("zocial-cart") //
        , chrome("zocial-chrome") //
        , cloudapp("zocial-cloudapp") //
        , creativecommons("zocial-creativecommons") //
        , delicious("zocial-delicious") //
        , digg("zocial-digg") //
        , disqus("zocial-disqus") //
        , dribbble("zocial-dribbble") //
        , dropbox("zocial-dropbox") //
        , drupal("zocial-drupal") //
        , dwolla("zocial-dwolla") //
        , email("zocial-email") //
        , eventasaurus("zocial-eventasaurus") //
        , eventbrite("zocial-eventbrite") //
        , eventful("zocial-eventful") //
        , evernote("zocial-evernote") //
        , facebook("zocial-facebook") //
        , fivehundredpx("zocial-fivehundredpx") //
        , flattr("zocial-flattr") //
        , flickr("zocial-flickr") //
        , forrst("zocial-forrst") //
        , foursquare("zocial-foursquare") //
        , github("zocial-github") //
        , gmail("zocial-gmail") //
        , google("zocial-google") //
        , googleplay("zocial-googleplay") //
        , googleplus("zocial-googleplus") //
        , gowalla("zocial-gowalla") //
        , grooveshark("zocial-grooveshark") //
        , guest("zocial-guest") //
        , html5("zocial-html5") //
        , ie("zocial-ie") //
        , instagram("zocial-instagram") //
        , instapaper("zocial-instapaper") //
        , intensedebate("zocial-intensedebate") //
        , itunes("zocial-itunes") //
        , klout("zocial-klout") //
        , lanyrd("zocial-lanyrd") //
        , lastfm("zocial-lastfm") //
        , linkedin("zocial-linkedin") //
        , macstore("zocial-macstore") //
        , meetup("zocial-meetup") //
        , myspace("zocial-myspace") //
        , ninetyninedesigns("zocial-ninetyninedesigns") //
        , openid("zocial-openid") //
        , opentable("zocial-opentable") //
        , paypal("zocial-paypal") //
        , pinboard("zocial-pinboard") //
        , pinterest("zocial-pinterest") //
        , plancast("zocial-plancast") //
        , plurk("zocial-plurk") //
        , pocket("zocial-pocket") //
        , podcast("zocial-podcast") //
        , posterous("zocial-posterous") //
        , print("zocial-print") //
        , quora("zocial-quora") //
        , reddit("zocial-reddit") //
        , rss("zocial-rss") //
        , scribd("zocial-scribd") //
        , skype("zocial-skype") //
        , smashing("zocial-smashing") //
        , songkick("zocial-songkick") //
        , soundcloud("zocial-soundcloud") //
        , spotify("zocial-spotify") //
        , statusnet("zocial-statusnet") //
        , steam("zocial-steam") //
        , stripe("zocial-stripe") //
        , stumbleupon("zocial-stumbleupon") //
        , tumblr("zocial-tumblr") //
        , twitter("zocial-twitter") //
        , viadeo("zocial-viadeo") //
        , vimeo("zocial-vimeo") //
        , vk("zocial-vk") //
        , weibo("zocial-weibo") //
        , wikipedia("zocial-wikipedia") //
        , windows("zocial-windows") //
        , wordpress("zocial-wordpress") //
        , xing("zocial-xing") //
        , yahoo("zocial-yahoo") //
        , yelp("zocial-yelp") //
        , youtube("zocial-youtube") //
        , dribbble_example("zocial-dribbble example") //
        ;
        private final String code;

        Zocial(String code) {
            this.code = code;
        }

        @Override
        public String getCode() {
            return this.code;
        }
    }
}
