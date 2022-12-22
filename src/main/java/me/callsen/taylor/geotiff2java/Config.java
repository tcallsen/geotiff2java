package me.callsen.taylor.geotiff2java;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONObject;

public class Config extends JSONObject {
	
  static String configPath = System.getenv("CONFIG_PATH") != null ? System.getenv("CONFIG_PATH") : "/development/workspace/config.json";

	public Config() throws Exception {
    super( new String(Files.readAllBytes(Paths.get(configPath)), Charset.defaultCharset()) );
	}
	
	public JSONObject getDbConfig() {
		return this.getJSONObject("postgis");
	}

  public JSONObject getCompTableConfig() {
		return this.getJSONObject("adjacentRoads").getJSONObject("compTable");
	}

}
