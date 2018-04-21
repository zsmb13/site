package co.zsmb.site.backend.handlers.util

import reactor.util.function.*

operator fun <T1, T2> Tuple2<T1, T2>.component1() = t1
operator fun <T1, T2> Tuple2<T1, T2>.component2() = t2
operator fun <T1, T2, T3> Tuple3<T1, T2, T3>.component3() = t3
operator fun <T1, T2, T3, T4> Tuple4<T1, T2, T3, T4>.component4() = t4
operator fun <T1, T2, T3, T4, T5> Tuple5<T1, T2, T3, T4, T5>.component5() = t5
operator fun <T1, T2, T3, T4, T5, T6> Tuple6<T1, T2, T3, T4, T5, T6>.component6() = t6
operator fun <T1, T2, T3, T4, T5, T6, T7> Tuple7<T1, T2, T3, T4, T5, T6, T7>.component7() = t7
operator fun <T1, T2, T3, T4, T5, T6, T7, T8> Tuple8<T1, T2, T3, T4, T5, T6, T7, T8>.component8() = t8
