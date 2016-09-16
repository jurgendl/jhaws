package org.jhaws.common.batch;

import java.time.Duration;
import java.util.Date;

import org.jhaws.common.lang.DateTime8;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BatchExecution<T extends Runnable> implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(BatchExecution.class);

	private BatchStep step;

	private T action;

	private boolean throwsException = true;

	public BatchExecution() {
		super();
	}

	public BatchExecution(BatchStep step, T action, boolean throwsException) {
		this.step = step;
		this.action = action;
		this.throwsException = throwsException;
	}

	@Override
	public void run() {
		String exception = null;
		Date start = new Date();
		String fullId = step.fullId();
		try {
			logger.info("start: {} {}", fullId, start);
			step.setStart(start);
			step.setState(BatchState.busy);
			action.run();
			step.setState(BatchState.done);
			step.progress();
		} catch (RuntimeException ex) {
			logger.info("error throw {}: {}", throwsException, fullId, ex);
			step.setState(BatchState.error);
			exception = ex.getLocalizedMessage();
			if (throwsException) {
				throw ex;
			}
		} finally {
			Date end = new Date();
			step.setEnd(end);
			long millis = start.getTime() - end.getTime();
			String duration = (millis < 1000l) ? null : DateTime8.printShort(Duration.ofMillis(millis), null);
			if (exception != null && duration != null) {
				step.setInfo("<" + duration + ">" + exception);
			} else if (duration != null) {
				step.setInfo("<" + duration + ">");
			} else if (exception != null) {
				step.setInfo(exception);
			}
			logger.info("end: {} {}", fullId, end);
		}
	}

	public BatchStep getStep() {
		return step;
	}

	public void setStep(BatchStep step) {
		this.step = step;
	}

	public T getAction() {
		return action;
	}

	public void setAction(T action) {
		this.action = action;
	}

	public boolean getThrowsException() {
		return throwsException;
	}

	public void setThrowsException(boolean throwsException) {
		this.throwsException = throwsException;
	}

	@Override
	public String toString() {
		return "BatchExecution[" + throwsException + ":" + step + "]";
	}
}
