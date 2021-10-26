import java.io.*;
import java.math.*;
import java.security.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;
import java.util.regex.*;
import java.util.stream.*;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;



class Result {

    /*
     * Complete the 'getTotalGoals' function below.
     *
     * The function is expected to return an INTEGER.
     * The function accepts following parameters:
     *  1. STRING team
     *  2. INTEGER year
     */
    public static int getCount(String API, String team, int flg) {
        int page = 1;
        int ans = 0;
        while(true) {
            
            try {

                URL url = new URL(API + Integer.toString(page));
                
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.connect();
                
                int responsecode = conn.getResponseCode();
                if (responsecode != 200) {
                    break;
                } else {

                    String inline = "";
                    Scanner scanner = new Scanner(url.openStream());

                    //Write all the JSON data into a string using a scanner
                    while (scanner.hasNext()) {
                        inline += scanner.nextLine();
                    }

                    //Close the scanner
                    scanner.close();

                    //Using the JSON simple library parse the string into a json object
                    JSONParser parse = new JSONParser();
                    JSONObject data_obj = (JSONObject) parse.parse(inline);


                    JSONArray data = (JSONArray) data_obj.get("data");
                    if( data.size() == 0 ) break;
                
                    for (int i = 0; i < data.size(); i++) {

                        JSONObject new_obj = (JSONObject) data.get(i);
                        
                        if (flg == 1 && ((String)new_obj.get("team1")).toLowerCase().equals(team)) {
                            ans = ans + Integer.parseInt( (String) new_obj.get("team1goals") );
                        }
                        if (flg == 2 && ((String)new_obj.get("team2")).toLowerCase().equals(team)) {
                            ans = ans + Integer.parseInt( (String) new_obj.get("team2goals") );
                        }
                    }
                    page++;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
            
        return ans;
    }
    public static String makeURL(String now) {
        String nw = "";
        for(int i =0 ; i < now.length(); i++) {
            if(now.charAt(i) == ' ') {
                nw += "%20";
            } else nw += now.charAt(i);
        }
        return nw;
    }
    public static int getTotalGoals(String team, int year) {
        team = team.toLowerCase();
        String APITeam1 = "https://jsonmock.hackerrank.com/api/football_matches?year=" + Integer.toString(year) + "&team1=" + makeURL(team) + "&page=";
        String APITeam2 = "https://jsonmock.hackerrank.com/api/football_matches?year=" + Integer.toString(year) + "&team2=" + makeURL(team) + "&page=";
        return getCount(APITeam1, team, 1) + getCount(APITeam2, team, 2);
    }

}
