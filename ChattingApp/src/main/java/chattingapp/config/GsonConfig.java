/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chattingapp.config;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.fatboyindustrial.gsonjavatime.Converters;

public class GsonConfig {
    private static final Gson GSON = Converters.registerAll(new GsonBuilder())
                                              .create();

    public static Gson getGson() {
        return GSON;
    }
}