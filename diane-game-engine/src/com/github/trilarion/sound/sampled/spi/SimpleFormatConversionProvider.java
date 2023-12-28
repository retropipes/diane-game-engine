/*
 * Copyright (C) 1999 - 2004 by Matthias Pfisterer
 *               2015 Trilarion
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.trilarion.sound.sampled.spi;

import java.util.ArrayList;
import java.util.Collection;

import javax.sound.sampled.AudioFormat;

import com.github.trilarion.sound.sampled.AudioFormats;

/**
 * This is a base class for FormatConversionProviders that can convert from each
 * source encoding/format to each target encoding/format. If this is not the
 * case, use TEncodingFormatConversionProvider.
 *
 * <p>
 * Overriding classes must provide a constructor that calls the protected
 * constructor of this class and override
 * <code>AudioInputStream getAudioInputStream(AudioFormat targetFormat, AudioInputStream sourceStream)</code>.
 * The latter method should be able to handle the case that all fields are
 * NOT_SPECIFIED and provide appropriate default values.
 *
 * @author Matthias Pfisterer
 */
// todo:
// - declare a constant ALL_BUT_SAME_VALUE (==-2) or so that can be used in
// format lists
// - consistent implementation of replacing NOT_SPECIFIED when not given in
// conversion
public abstract class SimpleFormatConversionProvider extends TFormatConversionProvider {
    private static void collectEncodings(final Collection<AudioFormat> formats,
	    final Collection<AudioFormat.Encoding> encodings) {
	for (final AudioFormat format : formats) {
	    encodings.add(format.getEncoding());
	}
    }

    private final Collection<AudioFormat.Encoding> m_sourceEncodings;
    private final Collection<AudioFormat.Encoding> m_targetEncodings;
    private final Collection<AudioFormat> m_sourceFormats;
    private final Collection<AudioFormat> m_targetFormats;

    /**
     *
     * @param sourceFormats
     * @param targetFormats
     */
    protected SimpleFormatConversionProvider(Collection<AudioFormat> sourceFormats,
	    Collection<AudioFormat> targetFormats) {
	this.m_sourceEncodings = new ArrayList<>();
	this.m_targetEncodings = new ArrayList<>();
	if (sourceFormats == null) {
	    sourceFormats = new ArrayList<>();
	}
	if (targetFormats == null) {
	    targetFormats = new ArrayList<>();
	}
	this.m_sourceFormats = sourceFormats;
	this.m_targetFormats = targetFormats;
	SimpleFormatConversionProvider.collectEncodings(this.m_sourceFormats, this.m_sourceEncodings);
	SimpleFormatConversionProvider.collectEncodings(this.m_targetFormats, this.m_targetEncodings);
    }

    @Override
    public AudioFormat.Encoding[] getSourceEncodings() {
	return this.m_sourceEncodings.toArray(TFormatConversionProvider.EMPTY_ENCODING_ARRAY);
    }

    @Override
    public AudioFormat.Encoding[] getTargetEncodings() {
	return this.m_targetEncodings.toArray(TFormatConversionProvider.EMPTY_ENCODING_ARRAY);
    }

    /**
     * This implementation assumes that the converter can convert from each of its
     * source encodings to each of its target encodings. If this is not the case,
     * the converter has to override this method.
     *
     * @param sourceFormat
     * @return
     */
    @Override
    public AudioFormat.Encoding[] getTargetEncodings(final AudioFormat sourceFormat) {
	if (this.isAllowedSourceFormat(sourceFormat)) {
	    return this.getTargetEncodings();
	}
	return TFormatConversionProvider.EMPTY_ENCODING_ARRAY;
    }

    /**
     * This implementation assumes that the converter can convert from each of its
     * source formats to each of its target formats. If this is not the case, the
     * converter has to override this method.
     *
     * @param targetEncoding
     * @param sourceFormat
     * @return
     */
    @Override
    public AudioFormat[] getTargetFormats(final AudioFormat.Encoding targetEncoding, final AudioFormat sourceFormat) {
	if (this.isConversionSupported(targetEncoding, sourceFormat)) {
	    return this.m_targetFormats.toArray(TFormatConversionProvider.EMPTY_FORMAT_ARRAY);
	}
	return TFormatConversionProvider.EMPTY_FORMAT_ARRAY;
    }

    /**
     *
     * @param sourceFormat
     * @return
     */
    protected boolean isAllowedSourceFormat(final AudioFormat sourceFormat) {
	for (final AudioFormat format : this.m_sourceFormats) {
	    if (AudioFormats.matches(format, sourceFormat)) {
		return true;
	    }
	}
	return false;
    }

    // overwritten of FormatConversionProvider
    @Override
    public boolean isSourceEncodingSupported(final AudioFormat.Encoding sourceEncoding) {
	return this.m_sourceEncodings.contains(sourceEncoding);
    }

    // overwritten of FormatConversionProvider
    @Override
    public boolean isTargetEncodingSupported(final AudioFormat.Encoding targetEncoding) {
	return this.m_targetEncodings.contains(targetEncoding);
    }
}
