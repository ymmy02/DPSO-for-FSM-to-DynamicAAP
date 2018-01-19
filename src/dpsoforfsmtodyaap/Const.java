package dpsoforfsmtodyaap;

public final class Const {

	static final double LARGENEGATIVE = -1e15;
	static final int NSKIP            = 1;

	// --- FSM Parameters --- //
	static final int NUMOFSTATES      = 5;

	// --- Ant Simulator Parameters --- //
	static final int MAXSTEP          = 600;

	// --- PSO Parameters --- //
	static final int POPULATION       = 1000;
	//static final int MAXLOOP          = 250;   // MAXLOOP is defined by NENVCHANGE
	static final double VMIN          = -2.0;
	static final double VMAX          = 2.0;
	static final double W             = 0.0;
	static final double C1            = 2.0;
	static final double C2            = 2.0;
	
	// --- Multi Swarm Parameters --- //
	static final int NUMOFSWARMS      = 5;
	static final double R_CLOUD       = 0.6;   // Ratio: 0 < R_EXCL < 1.0
	static final double R_EXCL        = 0.6;   // Ratio: 0 < R_EXCL < 1.0
	
	// --- Dynamic Environment Parameters --- //
	static final int NENVCHANGE       = 50;

}
