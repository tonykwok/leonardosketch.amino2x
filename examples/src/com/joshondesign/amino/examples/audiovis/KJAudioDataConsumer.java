/*
 * Created on Jul 10, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package com.joshondesign.amino.examples.audiovis;

/**
 * @author Kris
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public interface KJAudioDataConsumer {
	
	void writeAudioData( byte[] pAudioData );
	void writeAudioData( byte[] pAudioData, int pOffset, int pLength );
	
}
