package com.xtremelabs.robolectric.shadows;

import android.content.res.Resources;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import com.xtremelabs.robolectric.R;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.WithTestDefaultsRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static com.xtremelabs.robolectric.Robolectric.shadowOf;
import static org.junit.Assert.*;

@RunWith(WithTestDefaultsRunner.class)
public class BitmapDrawableTest {
    private Resources resources;

    @Before
    public void setUp() throws Exception {
        resources = Robolectric.application.getResources();
    }

    @Test
    public void getBitmap_shouldReturnBitmapUsedToDraw() throws Exception {
        BitmapDrawable drawable = (BitmapDrawable) resources.getDrawable(R.drawable.an_image);
        assertEquals("Bitmap for resource:drawable/an_image", shadowOf(drawable.getBitmap()).getDescription());
    }

    @Test
    public void draw_shouldCopyDescriptionToCanvas() throws Exception {
        BitmapDrawable drawable = (BitmapDrawable) resources.getDrawable(R.drawable.an_image);
        Canvas canvas = new Canvas();
        drawable.draw(canvas);

        assertEquals("Bitmap for resource:drawable/an_image", shadowOf(canvas).getDescription());
    }

    @Test
    public void shouldInheritSourceStringFromDrawableDotCreateFromStream() throws Exception {
        InputStream emptyInputStream = new ByteArrayInputStream("".getBytes());
        BitmapDrawable drawable = (BitmapDrawable) Drawable.createFromStream(emptyInputStream, "source string value");
        assertEquals("source string value", shadowOf(drawable).getSource());
    }

    @Test
    public void withColorFilterSet_draw_shouldCopyDescriptionToCanvas() throws Exception {
        BitmapDrawable drawable = (BitmapDrawable) resources.getDrawable(R.drawable.an_image);
        drawable.setColorFilter(new ColorMatrixColorFilter(new ColorMatrix()));
        Canvas canvas = new Canvas();
        drawable.draw(canvas);

        assertEquals("Bitmap for resource:drawable/an_image with ColorMatrixColorFilter<1,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,1,0>",
                shadowOf(canvas).getDescription());
    }

    @Test
    public void equals_shouldTestResourceId() throws Exception {
        Drawable drawable1a = resources.getDrawable(R.drawable.an_image);
        Drawable drawable1b = resources.getDrawable(R.drawable.an_image);
        Drawable drawable2 = resources.getDrawable(R.drawable.an_other_image);

        assertEquals(drawable1a, drawable1b);
        assertFalse(drawable1a.equals(drawable2));
    }

    @Test
    public void equals_shouldTestBounds() throws Exception {
        Drawable drawable1a = resources.getDrawable(R.drawable.an_image);
        Drawable drawable1b = resources.getDrawable(R.drawable.an_image);

        drawable1a.setBounds(1, 2, 3, 4);
        drawable1b.setBounds(1, 2, 3, 4);

        assertEquals(drawable1a, drawable1b);

        drawable1b.setBounds(1, 2, 3, 5);
        assertFalse(drawable1a.equals(drawable1b));
    }

    @Test
    public void shouldStillHaveShadow() throws Exception {
        Drawable drawable = resources.getDrawable(R.drawable.an_image);
        assertEquals(R.drawable.an_image, Robolectric.shadowOf(drawable).getLoadedFromResourceId());
    }

    @Test
    public void shouldSetTileModeXY() throws Exception {
        BitmapDrawable drawable = (BitmapDrawable) resources.getDrawable(R.drawable.an_image);
        drawable.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.MIRROR);
        assertEquals(Shader.TileMode.REPEAT, drawable.getTileModeX());
        assertEquals(Shader.TileMode.MIRROR, drawable.getTileModeY());
    }

    @Test
    public void constructor_shouldSetTheIntrinsicWidthAndHeightToTheWidthAndHeightOfTheBitmap() throws Exception {
        Bitmap bitmap = Bitmap.createBitmap(5, 10, Bitmap.Config.ARGB_8888);
        BitmapDrawable drawable = new BitmapDrawable(Robolectric.application.getResources(), bitmap);
        assertEquals(5, drawable.getIntrinsicWidth());
        assertEquals(10, drawable.getIntrinsicHeight());
    }

    @Test
    public void constructor_shouldAcceptNullBitmap() throws Exception {
        BitmapDrawable drawable = new BitmapDrawable(Robolectric.application.getResources(), (Bitmap) null);
        assertNotNull(drawable);
    }
}
