/*
 * $Id: DefaultButtonImageResource.java 5791 2006-05-20 00:32:57 +0000 (Sat, 20
 * May 2006) joco01 $ $Revision: 5871 $ $Date: 2006-05-20 00:32:57 +0000 (Sat,
 * 20 May 2006) $
 * 
 * ==============================================================================
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package wicket.markup.html.image.resource;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/**
 * Automatically generates a basic button image. The model for the component
 * determines the label displayed on the button.
 * 
 * @author Jonathan Locke
 */
public class DefaultButtonImageResource extends RenderedDynamicImageResource
{
	private static final long serialVersionUID = 1L;

	/** The default height for button images */
	private static int defaultHeight = 26;

	/** The default width for button images */
	private static int defaultWidth = 74;

	/** The height of the arc in the corner */
	private int arcHeight = 10;

	/** The width of the arc in the corner */
	private int arcWidth = 10;

	/** The background color behind the button */
	private Color backgroundColor = Color.WHITE;

	/** The color of the button itself */
	private Color color = new Color(0xE9, 0x60, 0x1A);

	/** The font to use */
	private Font font = new Font("Helvetica", Font.BOLD, 16);

	/** The color of the text */
	private Color textColor = Color.WHITE;

	/** The button label */
	private final String label;

	/**
	 * @param defaultHeight
	 *            The defaultHeight to set.
	 */
	public static void setDefaultHeight(int defaultHeight)
	{
		DefaultButtonImageResource.defaultHeight = defaultHeight;
	}

	/**
	 * @param defaultWidth
	 *            The defaultWidth to set.
	 */
	public static void setDefaultWidth(int defaultWidth)
	{
		DefaultButtonImageResource.defaultWidth = defaultWidth;
	}

	/**
	 * @param label
	 *            The label for this button image
	 * @param width
	 *            Width of image in pixels
	 * @param height
	 *            Height of image in pixels
	 */
	public DefaultButtonImageResource(int width, int height, final String label)
	{
		super(width, height, "png");
		this.label = label;
		setWidth(width == -1 ? defaultWidth : width);
		setHeight(height == -1 ? defaultHeight : height);
	}

	/**
	 * @param label
	 *            The label for this button image
	 */
	public DefaultButtonImageResource(final String label)
	{
		this(defaultWidth, defaultHeight, label);
	}

	/**
	 * @return Returns the arcHeight.
	 */
	public synchronized int getArcHeight()
	{
		return arcHeight;
	}

	/**
	 * @return Returns the arcWidth.
	 */
	public synchronized int getArcWidth()
	{
		return arcWidth;
	}

	/**
	 * @return Returns the backgroundColor.
	 */
	public synchronized Color getBackgroundColor()
	{
		return backgroundColor;
	}

	/**
	 * @return Returns the color.
	 */
	public synchronized Color getColor()
	{
		return color;
	}

	/**
	 * @return Returns the font.
	 */
	public synchronized Font getFont()
	{
		return font;
	}

	/**
	 * @return Returns the textColor.
	 */
	public synchronized Color getTextColor()
	{
		return textColor;
	}

	/**
	 * @param arcHeight
	 *            The arcHeight to set.
	 */
	public synchronized void setArcHeight(int arcHeight)
	{
		this.arcHeight = arcHeight;
		invalidate();
	}

	/**
	 * @param arcWidth
	 *            The arcWidth to set.
	 */
	public synchronized void setArcWidth(int arcWidth)
	{
		this.arcWidth = arcWidth;
		invalidate();
	}

	/**
	 * @param backgroundColor
	 *            The backgroundColor to set.
	 */
	public synchronized void setBackgroundColor(Color backgroundColor)
	{
		this.backgroundColor = backgroundColor;
		invalidate();
	}

	/**
	 * @param color
	 *            The color to set.
	 */
	public synchronized void setColor(Color color)
	{
		this.color = color;
		invalidate();
	}

	/**
	 * @param font
	 *            The font to set.
	 */
	public synchronized void setFont(Font font)
	{
		this.font = font;
		invalidate();
	}

	/**
	 * @param textColor
	 *            The textColor to set.
	 */
	public synchronized void setTextColor(Color textColor)
	{
		this.textColor = textColor;
		invalidate();
	}

	/**
	 * Renders button image.
	 * 
	 * @see RenderedDynamicImageResource#render(Graphics2D)
	 */
	@Override
	protected boolean render(final Graphics2D graphics)
	{
		// Get width and height
		final int width = getWidth();
		final int height = getHeight();

		// Get size of text
		graphics.setFont(font);
		final FontMetrics fontMetrics = graphics.getFontMetrics();
		final int dxText = fontMetrics.stringWidth(label);
		final int dxMargin = 10;

		// Does text fit with a nice margin?
		if (dxText > width - dxMargin)
		{
			// Re-render as a larger button
			setWidth(dxText + dxMargin);
			return false;
		}
		else
		{
			// Turn on anti-aliasing
			graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);

			// Draw background
			graphics.setColor(backgroundColor);
			graphics.fillRect(0, 0, width, height);

			// Draw round rectangle
			graphics.setColor(color);
			graphics.setBackground(backgroundColor);
			graphics.fillRoundRect(0, 0, width, height, arcWidth, arcHeight);

			// Draw text
			graphics.setColor(textColor);
			final int x = (width - dxText) / 2;
			final int y = (getHeight() - fontMetrics.getHeight()) / 2;
			graphics.drawString(label, x, y + fontMetrics.getAscent());
			return true;
		}
	}
}
