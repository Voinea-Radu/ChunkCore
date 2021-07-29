package dev.lightdream.ultraprisoncore.gangs.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.UUID;

public class GsonUtils {
    private static Gson gson;

    public GsonUtils() {
    }

    public static Gson getGson() {
        if (gson == null) {
            gson = (new GsonBuilder()).registerTypeAdapter(Location.class, new GsonUtils.LocationAdapter()).create();
        }

        return gson;
    }

    private static class DeserializedLocation {
        World world = null;
        double x = 0.0D;
        double y = 0.0D;
        double z = 0.0D;
        float yaw = 0.0F;
        float pitch = 0.0F;

        public DeserializedLocation() {
        }

        public void setWorld(World var1) {
            this.world = var1;
        }

        public void setX(double var1) {
            this.x = var1;
        }

        public void setY(double var1) {
            this.y = var1;
        }

        public void setZ(double var1) {
            this.z = var1;
        }

        public void setYaw(float var1) {
            this.yaw = var1;
        }

        public void setPitch(float var1) {
            this.pitch = var1;
        }

        public boolean isValid() {
            return this.world != null;
        }

        public Location getLocation() {
            return new Location(this.world, this.x, this.y, this.z, this.yaw, this.pitch);
        }
    }

    private static class LocationAdapter extends TypeAdapter<Location> {
        private LocationAdapter() {
        }

        @SneakyThrows
        public void write(JsonWriter var1, Location var2) {
            var1.beginObject();
            var1.name("uuid").value(var2.getWorld().getUID().toString());
            var1.name("x").value(Double.toString(var2.getX()));
            var1.name("y").value(Double.toString(var2.getY()));
            var1.name("z").value(Double.toString(var2.getZ()));
            var1.name("yaw").value(Float.toString(var2.getYaw()));
            var1.name("pitch").value(Float.toString(var2.getPitch()));
            var1.endObject();
        }

        @SneakyThrows
        public Location read(JsonReader var1) {
            GsonUtils.DeserializedLocation var2 = new GsonUtils.DeserializedLocation();
            var1.beginObject();

            while (var1.hasNext()) {
                String var3 = var1.nextName();
                byte var4 = -1;
                switch (var3.hashCode()) {
                    case 120:
                        if (var3.equals("x")) {
                            var4 = 1;
                        }
                        break;
                    case 121:
                        if (var3.equals("y")) {
                            var4 = 2;
                        }
                        break;
                    case 122:
                        if (var3.equals("z")) {
                            var4 = 3;
                        }
                        break;
                    case 119407:
                        if (var3.equals("yaw")) {
                            var4 = 4;
                        }
                        break;
                    case 3601339:
                        if (var3.equals("uuid")) {
                            var4 = 0;
                        }
                        break;
                    case 106677056:
                        if (var3.equals("pitch")) {
                            var4 = 5;
                        }
                }

                switch (var4) {
                    case 0:
                        var2.setWorld(Bukkit.getWorld(UUID.fromString(var1.nextString())));
                        break;
                    case 1:
                        var2.setX(var1.nextDouble());
                        break;
                    case 2:
                        var2.setY(var1.nextDouble());
                        break;
                    case 3:
                        var2.setZ(var1.nextDouble());
                        break;
                    case 4:
                        var2.setYaw(Float.parseFloat(var1.nextString()));
                        break;
                    case 5:
                        var2.setPitch(Float.parseFloat(var1.nextString()));
                }
            }

            var1.endObject();
            if (var2.isValid()) {
                return var2.getLocation();
            } else {
                return null;
            }
        }
    }
}
