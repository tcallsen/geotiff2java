package me.callsen.taylor.geotiff2java;

import java.awt.image.BufferedImage;
import java.io.File;
import java.time.Duration;
import java.time.Instant;

import org.apache.commons.imaging.formats.tiff.TiffImageParser;
import org.apache.commons.imaging.formats.tiff.TiffImagingParameters;
import org.geotools.coverage.grid.GridCoordinates2D;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.InvalidGridGeometryException;
import org.geotools.coverage.grid.io.imageio.IIOMetadataDumper;
import org.geotools.gce.geotiff.GeoTiffReader;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.Envelope2D;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.locationtech.jts.geom.Coordinate;
import org.opengis.geometry.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.opengis.coverage.grid.GridEnvelope;
import org.opengis.geometry.DirectPosition;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

public class App {

  private static GridCoverage2D cov;
	public static void main( String[] args ) throws Exception {
		
		Instant start = Instant.now();
		
    // read tiff file into memory
    // http://www.smartjava.org/content/access-information-geotiff-using-java/
    File tiffFile = new File("/development/workspace/USGS_13_n38w123_uncomp.tif");
    GeoTiffReader reader = new GeoTiffReader(tiffFile);
    cov = reader.read(null);
    
    
    Envelope2D envelope = cov.getEnvelope2D();
    IIOMetadataDumper dumper = new IIOMetadataDumper(reader.getMetadata().getRootNode());
    
    // follow logic in this link to convert gps coords to pixel:
    // https://stackoverflow.com/questions/50640719/how-do-i-extract-the-latitude-and-longitude-values-from-geotiff-image-using-java

    // DATA


    // CoordinateReferenceSystem wgs84 = DefaultGeographicCRS.WGS84;
    // CoordinateReferenceSystem target = cov.getCoordinateReferenceSystem();
    
    // GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory( null );
    // MathTransform targetToWgs = CRS.findMathTransform(wgs84, target);
    
    

    // DirectPosition posWorld = new DirectPosition2D(wgs84, 37.739154d,-122.447067d);
    

    // gg.gridToWorld(new GridCoordinates2D(6800,6000))

    // https://stackoverflow.com/questions/15429011/how-to-convert-tiff-to-jpeg-png-in-java
    // SeekableStream s = new FileSeekableStream(tiffFile);
    // TIFFDecodeParam param = null;
    // ImageDecoder dec = ImageCodec.createImageDecoder("tiff", s, param);
    // RenderedImage op = dec.decodeAsRenderedImage(0);

    // https://stackoverflow.com/questions/64537498/get-tiff-tag-value-including-non-ascii-characters-from-tiff-images-in-java-11
    // try (ImageInputStream input = ImageIO.createImageInputStream(tiffFile)) {
    //     ImageReader reader = ImageIO.getImageReaders(input).next(); // TODO: Handle reader not found
    //     reader.setInput(input);
    //     IIOMetadata metadata = reader.getImageMetadata(0); // 0 is the index of first image
    //     reader.read(0);
    // }

    // https://gis.stackexchange.com/questions/119134/looking-for-an-open-source-java-based-geotiff-library
    // final TiffImagingParameters params = new TiffImagingParameters();
    // TiffImageParser readerT = new TiffImageParser();
    // BufferedImage image = readerT.getBufferedImage(tiffFile, params);

    System.out.println(Duration.between(start, Instant.now() ));
  
  }

  public static double[] getElevationAtCoordinate(double lon, double lat) throws InvalidGridGeometryException, TransformException {
    CoordinateReferenceSystem wgs84 = DefaultGeographicCRS.WGS84;
    GridGeometry2D gg = cov.getGridGeometry();

    DirectPosition2D posWorld = new DirectPosition2D(wgs84, lon, lat);
    GridCoordinates2D posGrid = gg.worldToGrid(posWorld);
    return cov.getRenderedImage().getData().getPixel(posGrid.x, posGrid.y, new double[1]);
  }
  
}
