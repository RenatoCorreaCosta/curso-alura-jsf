package com.rc.curso_alura.util;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

public class LogPhaseListener implements PhaseListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void afterPhase(PhaseEvent event) {
		// TODO Auto-generated method stub
		System.out.println("after FASE: " + event.getPhaseId().toString() + " página: " + event.getFacesContext().getExternalContext().getRequestServletPath() );
	}

	@Override
	public void beforePhase(PhaseEvent event) {
		// TODO Auto-generated method stub
		System.out.println("before FASE: " + event.getPhaseId().toString() + " página: " + event.getFacesContext().getExternalContext().getRequestServletPath() );
	}

	@Override
	public PhaseId getPhaseId() {
		// TODO Auto-generated method stub
		return PhaseId.ANY_PHASE;
	}

}
