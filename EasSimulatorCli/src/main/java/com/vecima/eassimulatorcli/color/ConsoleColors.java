/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vecima.eassimulatorcli.color;

/**
 *
 * @author zayeed
 */
public class ConsoleColors {
    private static final boolean console_color = false;                                  // compiling jar for windows, this field will be set to false
    // Reset
    public static final String RESET = (console_color)?"\033[0m":"";                    // Text Reset

    // Regular Colors
    public static final String BLACK = (console_color)?"\033[0;30m":"";                 // BLACK
    public static final String RED = (console_color)?"\033[0;31m":"";                   // RED
    public static final String GREEN = (console_color)?"\033[0;32m":"";                 // GREEN
    public static final String YELLOW = (console_color)?"\033[0;33m":"";                // YELLOW
    public static final String BLUE = (console_color)?"\033[0;34m":"";                  // BLUE
    public static final String PURPLE = (console_color)?"\033[0;35m":"";                // PURPLE
    public static final String CYAN = (console_color)?"\033[0;36m":"";                  // CYAN
    public static final String WHITE = (console_color)?"\033[0;37m":"";                 // WHITE

    // Bold
    public static final String BLACK_BOLD = (console_color)?"\033[1;30m":"";            // BLACK
    public static final String RED_BOLD = (console_color)?"\033[1;31m":"";              // RED
    public static final String GREEN_BOLD = (console_color)?"\033[1;32m":"";            // GREEN
    public static final String YELLOW_BOLD = (console_color)?"\033[1;33m":"";           // YELLOW
    public static final String BLUE_BOLD = (console_color)?"\033[1;34m":"";             // BLUE
    public static final String PURPLE_BOLD = (console_color)?"\033[1;35m":"";           // PURPLE
    public static final String CYAN_BOLD = (console_color)?"\033[1;36m":"";             // CYAN
    public static final String WHITE_BOLD = (console_color)?"\033[1;37m":"";            // WHITE

    // Underline
    public static final String BLACK_UNDERLINED = (console_color)?"\033[4;30m":"";      // BLACK
    public static final String RED_UNDERLINED = (console_color)?"\033[4;31m":"";        // RED
    public static final String GREEN_UNDERLINED = (console_color)?"\033[4;32m":"";      // GREEN
    public static final String YELLOW_UNDERLINED = (console_color)?"\033[4;33m":"";     // YELLOW
    public static final String BLUE_UNDERLINED = (console_color)?"\033[4;34m":"";       // BLUE
    public static final String PURPLE_UNDERLINED = (console_color)?"\033[4;35m":"";     // PURPLE
    public static final String CYAN_UNDERLINED = (console_color)?"\033[4;36m":"";       // CYAN
    public static final String WHITE_UNDERLINED = (console_color)?"\033[4;37m":"";      // WHITE

    // Background
    public static final String BLACK_BACKGROUND = (console_color)?"\033[40m":"";        // BLACK
    public static final String RED_BACKGROUND = (console_color)?"\033[41m":"";          // RED
    public static final String GREEN_BACKGROUND = (console_color)?"\033[42m":"";        // GREEN
    public static final String YELLOW_BACKGROUND = (console_color)?"\033[43m":"";       // YELLOW
    public static final String BLUE_BACKGROUND = (console_color)?"\033[44m":"";         // BLUE
    public static final String PURPLE_BACKGROUND = (console_color)?"\033[45m":"";       // PURPLE
    public static final String CYAN_BACKGROUND = (console_color)?"\033[46m":"";         // CYAN
    public static final String WHITE_BACKGROUND = (console_color)?"\033[47m":"";        // WHITE

    // High Intensity
    public static final String BLACK_BRIGHT = (console_color)?"\033[0;90m":"";          // BLACK
    public static final String RED_BRIGHT = (console_color)?"\033[0;91m":"";            // RED
    public static final String GREEN_BRIGHT = (console_color)?"\033[0;92m":"";          // GREEN
    public static final String YELLOW_BRIGHT = (console_color)?"\033[0;93m":"";         // YELLOW
    public static final String BLUE_BRIGHT = (console_color)?"\033[0;94m":"";           // BLUE
    public static final String PURPLE_BRIGHT = (console_color)?"\033[0;95m":"";         // PURPLE
    public static final String CYAN_BRIGHT = (console_color)?"\033[0;96m":"";           // CYAN
    public static final String WHITE_BRIGHT = (console_color)?"\033[0;97m":"";          // WHITE

    // Bold High Intensity
    public static final String BLACK_BOLD_BRIGHT = (console_color)?"\033[1;90m":"";     // BLACK
    public static final String RED_BOLD_BRIGHT = (console_color)?"\033[1;91m":"";       // RED
    public static final String GREEN_BOLD_BRIGHT = (console_color)?"\033[1;92m":"";     // GREEN
    public static final String YELLOW_BOLD_BRIGHT = (console_color)?"\033[1;93m":"";    // YELLOW
    public static final String BLUE_BOLD_BRIGHT = (console_color)?"\033[1;94m":"";      // BLUE
    public static final String PURPLE_BOLD_BRIGHT = (console_color)?"\033[1;95m":"";    // PURPLE
    public static final String CYAN_BOLD_BRIGHT = (console_color)?"\033[1;96m":"";      // CYAN
    public static final String WHITE_BOLD_BRIGHT = (console_color)?"\033[1;97m":"";     // WHITE

    // High Intensity backgrounds
    public static final String BLACK_BACKGROUND_BRIGHT = (console_color)?"\033[0;100m":"";  // BLACK
    public static final String RED_BACKGROUND_BRIGHT = (console_color)?"\033[0;101m":"";    // RED
    public static final String GREEN_BACKGROUND_BRIGHT = (console_color)?"\033[0;102m":"";  // GREEN
    public static final String YELLOW_BACKGROUND_BRIGHT = (console_color)?"\033[0;103m":""; // YELLOW
    public static final String BLUE_BACKGROUND_BRIGHT = (console_color)?"\033[0;104m":"";   // BLUE
    public static final String PURPLE_BACKGROUND_BRIGHT = (console_color)?"\033[0;105m":""; // PURPLE
    public static final String CYAN_BACKGROUND_BRIGHT = (console_color)?"\033[0;106m":"";   // CYAN
    public static final String WHITE_BACKGROUND_BRIGHT = (console_color)?"\033[0;107m":"";  // WHITE
}