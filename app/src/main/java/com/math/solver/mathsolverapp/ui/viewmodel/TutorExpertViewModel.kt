package com.math.solver.mathsolverapp.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import com.math.solver.mathsolverapp.R
import com.math.solver.mathsolverapp.base.activity.Subject
import com.math.solver.mathsolverapp.base.viewmodel.BaseViewModel

class TutorExpertViewModel : BaseViewModel() {
    val expertSubjects = MutableLiveData<List<Subject>>()

    fun loadSubjects() {
        expertSubjects.value = listOf(
            Subject(R.drawable.ic_math_2,"Math" ),
            Subject( R.drawable.ic_physics," Physics"),
            Subject( R.drawable.ic_literature," Literature"),
            Subject( R.drawable.ic_chemistry," Chemistry"),
            Subject(R.drawable.ic_math_2," Geography" ),
            Subject(R.drawable.ic_language," Language" ),
            Subject( R.drawable.ic_economics," Economics"),
            Subject(R.drawable.ic_civics," Civics" ),
            Subject(R.drawable.ic_history," History" ),


            )
    }
}