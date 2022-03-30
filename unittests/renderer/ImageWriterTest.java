package renderer;

import primitives.Color;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import primitives.*;

/**
 * Unit tests for renderer.ImageWriter class
 * @author Nechama Eri-Barron and Chaya Yazersky
 */
public class ImageWriterTest {
    /**
     * Test method for {@link ImageWriter#writeToImage().
     * This method creates the image in png format
     */
    @Test
    public void testWriteToImage(){
        int nx = 800;
        int ny = 500;
        ImageWriter imageWriter = new ImageWriter("test yellow with red grid", nx, ny);

        //for every row
        for (int i = 0; i < nx; i++) {
            //for every column
            for (int j = 0; j < ny; j++) {
                //grid: 800/50 = 16, 500/50 = 10
                if((i%50 ==0) || (j % 50 ==0)){
                    imageWriter.writePixel(i,j, Color.RED);
                }
                else{imageWriter.writePixel(i, j, Color.YELLOW);}
            }
        }
        imageWriter.writeToImage();
    }
}
