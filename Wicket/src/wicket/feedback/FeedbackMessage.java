/*
 * $Id: FeedbackMessage.java 2913 2005-10-02 03:06:33 -0700 (Sun, 02 Oct 2005)
 * joco01 $ $Revision: 5967 $ $Date: 2005-10-02 03:06:33 -0700 (Sun, 02 Oct
 * 2005) $
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
package wicket.feedback;

import java.io.Serializable;

import wicket.Component;

/**
 * Represents a generic message meant for the end-user/ pages.
 * 
 * @author Eelco Hillenius
 * @author Jonathan Locke
 */
public class FeedbackMessage implements Serializable
{
	private static final long serialVersionUID = 1L;

	/** Constant for debug level. */
	public static final int DEBUG = 1;

	/** Constant for error level. */
	public static final int ERROR = 4;

	/** Constant for fatal level. */
	public static final int FATAL = 5;

	/** Constant for info level. */
	public static final int INFO = 2;

	/**
	 * Constant for an undefined level; note that components might decide not to
	 * render anything when this level is used.
	 */
	public static final int UNDEFINED = 0;

	/** Constant for warning level. */
	public static final int WARNING = 3;

	/** Levels as strings for debugging. */
	private static final String[] levelStrings = new String[] { "UNDEFINED", "DEBUG", "INFO",
			"WARNING", "ERROR", "FATAL" };

	/**
	 * The message level; can be used by rendering components. Note that what
	 * actually happens with the level indication is totally up to the
	 * components that render messages like these. The default level is
	 * UNDEFINED.
	 */
	private final int level;

	/** The actual message. */
	private final String message;

	/** The reporting component. */
	private final Component reporter;

	/** Whether or not this message has been rendered */
	private boolean rendered = false;

	/**
	 * Construct using fields.
	 * 
	 * @param reporter
	 *            The message reporter
	 * @param message
	 *            The actual message
	 * @param level
	 *            The level of the message
	 */
	public FeedbackMessage(final Component reporter, final String message, final int level)
	{
		this.reporter = reporter;
		this.message = message;
		this.level = level;
	}

	/**
	 * Gets whether or not this message has been rendered
	 * 
	 * @return true if this message has been rendered, false otherwise
	 */
	public final boolean isRendered()
	{
		return rendered;
	}


	/**
	 * Marks this message as rendered.
	 */
	public final void markRendered()
	{
		this.rendered = true;
	}


	/**
	 * Gets the message level; can be used by rendering components. Note that
	 * what actually happens with the level indication is totally up to the
	 * components that render feedback messages.
	 * 
	 * @return The message level indicator.
	 */
	public final int getLevel()
	{
		return level;
	}

	/**
	 * Gets the current level as a String
	 * 
	 * @return The current level as a String
	 */
	public String getLevelAsString()
	{
		return levelStrings[getLevel()];
	}

	/**
	 * Gets the actual message.
	 * 
	 * @return the message.
	 */
	public final String getMessage()
	{
		return message;
	}

	/**
	 * Gets the reporting component.
	 * 
	 * @return the reporting component.
	 */
	public final Component getReporter()
	{
		return reporter;
	}

	/**
	 * Gets whether the current level is DEBUG or up.
	 * 
	 * @return whether the current level is DEBUG or up.
	 */
	public final boolean isDebug()
	{
		return isLevel(DEBUG);
	}

	/**
	 * Gets whether the current level is ERROR or up.
	 * 
	 * @return whether the current level is ERROR or up.
	 */
	public final boolean isError()
	{
		return isLevel(ERROR);
	}

	/**
	 * Gets whether the current level is FATAL or up.
	 * 
	 * @return whether the current level is FATAL or up.
	 */
	public final boolean isFatal()
	{
		return isLevel(FATAL);
	}

	/**
	 * Gets whether the current level is INFO or up.
	 * 
	 * @return whether the current level is INFO or up.
	 */
	public final boolean isInfo()
	{
		return isLevel(INFO);
	}

	/**
	 * Returns whether this level is greater than or equal to the given level.
	 * 
	 * @param level
	 *            the level
	 * @return whether this level is greater than or equal to the given level
	 */
	public final boolean isLevel(int level)
	{
		return (getLevel() >= level);
	}

	/**
	 * Gets whether the current level is UNDEFINED.
	 * 
	 * @return whether the current level is UNDEFINED.
	 */
	public final boolean isUndefined()
	{
		return (getLevel() == UNDEFINED);
	}

	/**
	 * Gets whether the current level is WARNING or up.
	 * 
	 * @return whether the current level is WARNING or up.
	 */
	public final boolean isWarning()
	{
		return isLevel(WARNING);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "[FeedbackMessage message = \"" + getMessage() + "\", reporter = "
				+ getReporter().getId() + ", level = " + getLevelAsString() + "]";
	}
}
