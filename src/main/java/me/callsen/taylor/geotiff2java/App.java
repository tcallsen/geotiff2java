package me.callsen.taylor.geotiff2java;

import java.awt.image.Raster;
import java.io.File;

import org.geotools.coverage.grid.GridCoordinates2D;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.gce.geotiff.GeoTiffReader;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class App {

  public static void main( String[] args ) throws Exception {
    
    // based on: http://www.smartjava.org/content/access-information-geotiff-using-java/

    // load tiff file to memory
    File tiffFile = new File("/development/workspace/USGS_13_n38w123_uncomp.tif");
    GeoTiffReader reader = new GeoTiffReader(tiffFile);
    GridCoverage2D cov = reader.read(null);
    Raster tiffRaster = cov.getRenderedImage().getData();

    // convert lat/lon gps coordinates to tiff x/y coordinates
    CoordinateReferenceSystem wgs84 = DefaultGeographicCRS.WGS84;
    GridGeometry2D gg = cov.getGridGeometry();
    double lat = 37.75497;
    double lon = -122.44580;
    DirectPosition2D posWorld = new DirectPosition2D(wgs84, lon, lat); // longitute supplied first
    GridCoordinates2D posGrid = gg.worldToGrid(posWorld);

    // sample tiff data with at pixel coordinate
    double[] rasterData = new double[1];
    tiffRaster.getPixel(posGrid.x, posGrid.y, rasterData);

    System.out.println(String.format("GeoTIFF data at %s, %s: %s", lat, lon, rasterData[0]));
  }

}
