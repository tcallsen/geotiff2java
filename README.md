# Read GeoTIFF Data with Java GeoTools

Demonstrates how to read GeoTIFF data using the GeoTools library in Java. Raster data is retrieved for a particular latitude/longitude.

## GeoTIFF Compression

GeoTIFF files can be compressed to reduce filesize. I was not able to get the GeoTools library to read GeoTIFF files with LZW compression, and had to uncompress the file with GDAL prior to reading it in Java/GeoTools:

```
gdal_translate pathtoinput.tif pathforoutput.tif -co COMPRESS=NONE
```

I was able to read the LZW compressed files directly when using the GDAL Java bindings ([sample project](https://github.com/tcallsen/geotiff2java-gdal)).

For more information on GeoTIFF compression, check out: [https://kokoalberti.com/articles/geotiff-compression-optimization-guide/](https://kokoalberti.com/articles/geotiff-compression-optimization-guide/)

## Other Libraries

I experimented with a few other Java libraries for reading GeoTIFF files. The code snippets below show how to read the file into a `Raster` object. From there, the steps in `App.java` can be followed.

I also created another sample project that reads GeoTIFF data using the Java GDAL bindings: [https://github.com/tcallsen/geotiff2java-gdal](https://github.com/tcallsen/geotiff2java-gdal)

#### Java Advanced Imaging - no GeoTIFF LZW Compression support

```
// https://stackoverflow.com/questions/15429011/how-to-convert-tiff-to-jpeg-png-in-java
FileSeekableStream s = new FileSeekableStream(tiffFile);
TIFFDecodeParam param = null;
ImageDecoder dec = ImageCodec.createImageDecoder("tiff", s, param);
RenderedImage op = dec.decodeAsRenderedImage(0);
Raster tiffRaster = op.getData();
```

#### ImageIO - no GeoTIFF LZW Compression support

```
// https://stackoverflow.com/questions/64537498/get-tiff-tag-value-including-non-ascii-characters-from-tiff-images-in-java-11
try (ImageInputStream input = ImageIO.createImageInputStream(tiffFile)) {
  ImageReader reader = ImageIO.getImageReaders(input).next(); // TODO: Handle reader not found
  reader.setInput(input);
  // IIOMetadata metadata = reader.getImageMetadata(0); // 0 is the index of first image
  BufferedImage image = reader.read(0);
  Raster tiffRaster = image.getData();
}
```

#### Apache Commons Imaging - support for GeoTiff LZW compression but samples not as accurate

```
// https://gis.stackexchange.com/questions/119134/looking-for-an-open-source-java-based-geotiff-library
final TiffImagingParameters params = new TiffImagingParameters();
TiffImageParser readerT = new TiffImageParser();
BufferedImage image = readerT.getBufferedImage(tiffFile, params);
Raster tiffRaster = image.getData();
```