/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vecima.eassimulatorcli.util;

/**
 *
 * @author zayeed
 * Current FIPS locations for Alert
1. Pinal,AZ (004021) 
2. Maricopa,AZ (004013) 
3. Alpine,CA (006003)
4. United States (000000) 
5. California (006000) 
6. Butte,CA (006007) 
7. Alaska (002000) 
8. Denali,AK (002068) 
9. Delaware (010000) 
10. Massachusetts (025000) 
11. Florida (012000) 
12. Colorado (008000) 
13. Nevada (032000) 
14. Kentucky (021000) 
15. Adair,KY (021001) 
16. Allen,KY (021003) 
17. Anderson,KY (021005) 
18. Ballard,KY (021007) 
19. Barren,KY (021009) 
20. Bath,KY (021011) 
21. Bell,KY (021013) 
22. Boone,KY (021015) 
23. Bourbon,KY (021017) 
24. Boyle,KY (021021) 
25. Bracken,KY (021023) 
26. Breathitt,KY (021025) 
27. Breckinridge,KY (021027) 
28. Bullitt,KY (021029) 
29. Butler,KY (021031) 
30. Caldwell,KY (021033) 
31. Calloway,KY (021035)
 */
public class Locations {

    public static final String[][] STATE_CODES = new String[][]{
        {"004", "AZ"},
        {"006", "CA"},};
    public static final String[][] COUNTY_CODES = new String[][]{
        {"03", "Alpine"},
        {"13", "Maricopa"},
        {"21", "Pinal"},};

    private static final String[][] AREAS = new String[][]{
        {"004021", "Pinal, AZ"},
        {"004013", "Maricopa, AZ"},
        {"006003", "Alpine, CA"},
        {"000000", "United States"},
        {"006000", "California"},
        {"006007", "Butte, CA"},
        {"002000", "Alaska"},
        {"002068", "Denali, AK"},
        {"010000", "Delaware"},
        {"025000", "Massachusetts"},
        {"012000", "Florida"},
        {"008000", "Colorado"},
        {"032000", "Nevada"},
        {"021000", "Kentucky"},
        {"021001", "Adair, KY"},
        {"021003", "Allen, KY"},
        {"021005", "Anderson, KY"},
        {"021007", "Ballard, KY"},
        {"021009", "Barren, KY"},
        {"021011", "Bath, KY"},
        {"021013", "Bell, KY"},
        {"021015", "Boone, KY"},
        {"021017", "Bourbon, KY"},
        {"021021", "Boyle, KY"},
        {"021023", "Bracken,KY"},
        {"021025", "Breathitt, KY"},
        {"021027", "Breckinridge, KY"},
        {"021029", "Bullitt, KY"},
        {"021031", "Butler, KY"},
        {"021033", "Caldwell, KY"},
        {"021035", "Calloway, KY"},
        
    };
    /**
     * @return the STATE_CODES
     */
    public static String[][] getSTATE_CODES() {
        return STATE_CODES;
    }

    /**
     * @return the COUNTY_CODES
     */
    public static String[][] getCOUNTY_CODES() {
        return COUNTY_CODES;
    }

    /**
     * @return the AREAS
     */
    public static String[][] getAREAS() {
        return AREAS;
    }
}