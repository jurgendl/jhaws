package org.jhaws.common.batch;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class AbstractBatch implements Runnable {
	protected BatchExecution<RunnableList> execution;

	public AbstractBatch(String id) {
		BatchStep batch = new BatchStep(id);
		RunnableList action = new RunnableList();
		execution = new BatchExecution<>(batch, action, false);
	}

	public BatchExecution<RunnableList> getExecution() {
		return execution;
	}

	@Override
	public void run() {
		execution.run();
	}

	protected static BatchExecution<RunnableList> step(BatchExecution<RunnableList> parent, String id,
			boolean throwsException) {
		return step(parent, id, new RunnableList(), throwsException);
	}

	protected static BatchExecution<Runnable> step(BatchExecution<RunnableList> parent, String id,
			Supplier<String> supplyingAction, boolean throwsException) {
		BatchStep step = new BatchStep(id);
		return step(parent, step, () -> step.setInfo(supplyingAction.get()), throwsException);
	}

	protected static <R extends Runnable> BatchExecution<R> step(BatchExecution<RunnableList> parent, String id,
			R action, boolean throwsException) {
		return step(parent, new BatchStep(id), action, throwsException);
	}

	protected static <R extends Runnable> BatchExecution<R> step(BatchExecution<RunnableList> parent, BatchStep step,
			R action, boolean throwsException) {
		BatchExecution<R> exec = new BatchExecution<>(step, action, throwsException);
		parent.getStep().add(step);
		parent.getAction().add(exec);
		// System.out.println(parent + "//" + step + "//" + exec);
		return exec;
	}

	protected static BatchExecution<Runnable> step(BatchExecution<RunnableList> parent, String id, boolean act,
			Supplier<String> supplyingAction, boolean throwsException) {
		return Boolean.TRUE.equals(act) ? step(parent, id, supplyingAction, throwsException) : null;
	}

	protected static BatchExecution<Runnable> step(BatchExecution<RunnableList> parent, String id, boolean act,
			Runnable action, boolean throwsException) {
		return step(parent, id, act, () -> {
			action.run();
			return null;
		}, throwsException);
	}

	protected static <C> BatchExecution<Runnable> step(C context, BatchExecution<RunnableList> parent, String id,
			boolean act, Function<C, String> supplyingAction, boolean throwsException) {
		return step(parent, id, act, () -> supplyingAction.apply(context), throwsException);
	}

	protected static <C> BatchExecution<Runnable> step(C context, BatchExecution<RunnableList> parent, String id,
			boolean act, Consumer<C> supplyingAction, boolean throwsException) {
		return step(parent, id, act, () -> {
			supplyingAction.accept(context);
			return null;
		}, throwsException);
	}
}
