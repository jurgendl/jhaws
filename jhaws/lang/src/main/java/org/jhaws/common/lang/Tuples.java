package org.jhaws.common.lang;

@SuppressWarnings({ "rawtypes", "serial" })
public class Tuples extends Tuple5 {
	static public <T1, T2> Tuple2<T1, T2> append(Tuple1<T1> tuple, T2 t2) {
		return Tuple2.of(tuple.getT1(), t2);
	}

	static public <T1, T2, T3> Tuple3<T1, T2, T3> append(Tuple2<T1, T2> tuple, T3 t3) {
		return Tuple3.of(tuple.getT1(), tuple.getT2(), t3);
	}

	static public <T1, T2, T3, T4> Tuple4<T1, T2, T3, T4> append(Tuple3<T1, T2, T3> tuple, T4 t4) {
		return Tuple4.of(tuple.getT1(), tuple.getT2(), tuple.getT3(), t4);
	}

	static public <T1, T2, T3, T4, T5> Tuple5<T1, T2, T3, T4, T5> append(Tuple4<T1, T2, T3, T4> tuple, T5 t5) {
		return Tuple5.of(tuple.getT1(), tuple.getT2(), tuple.getT3(), tuple.getT4(), t5);
	}

//	static public <T1, T2, T3, T4, T5, T6> Tuple6<T1, T2, T3, T4, T5, T6> append(Tuple5<T1, T2, T3, T4, T5> tuple,
//			T6 t6) {
//		return Tuple6.of(tuple.getT1(), tuple.getT2(), tuple.getT3(), tuple.getT4(), tuple.getT5(), t6);
//	}
}
