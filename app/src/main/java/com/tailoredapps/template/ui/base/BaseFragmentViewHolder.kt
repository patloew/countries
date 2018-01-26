package com.tailoredapps.template.ui.base

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.RecyclerView
import android.view.View
import com.tailoredapps.template.BR
import com.tailoredapps.template.injection.components.DaggerFragmentViewHolderComponent
import com.tailoredapps.template.injection.components.FragmentViewHolderComponent
import com.tailoredapps.template.ui.base.view.MvvmView
import com.tailoredapps.template.ui.base.viewmodel.MvvmViewModel
import com.tailoredapps.template.util.extensions.attachViewOrThrowRuntimeException
import com.tailoredapps.template.util.extensions.castWithUnwrap
import timber.log.Timber
import javax.inject.Inject

/* Base class for ViewHolders when using a view model in a Fragment with data binding.
 * This class provides the binding and the view model to the subclass. The
 * view model is injected and the binding is created when the content view is bound.
 * Each subclass therefore has to call the following code in the constructor:
 *    bindContentView(view)
 *
 * After calling this method, the binding and the view model is initialized.
 * saveInstanceState() and restoreInstanceState() are not called/used for ViewHolder
 * view models.
 *
 * Your subclass must implement the MvvmView implementation that you use in your
 * view model. */
abstract class BaseFragmentViewHolder<B : ViewDataBinding, VM : MvvmViewModel<*>>(itemView: View) : RecyclerView.ViewHolder(itemView), MvvmView {

    protected lateinit var binding: B
    @Inject lateinit var viewModel: VM
        protected set

    protected abstract val fragmentContainerId : Int

    protected val viewHolderComponent: FragmentViewHolderComponent by lazy {
        DaggerFragmentViewHolderComponent.builder()
                .fragmentComponent(itemView.context.getFragment<BaseFragment<*,*>>(fragmentContainerId)!!.fragmentComponent)
                .build()
    }

    init {
        try {
            FragmentViewHolderComponent::class.java.getDeclaredMethod("inject", this::class.java).invoke(viewHolderComponent, this)
        } catch(e: NoSuchMethodException) {
            throw RtfmException("You forgot to add \"fun inject(viewHolder: ${this::class.java.simpleName})\" in FragmentViewHolderComponent")
        }
    }

    protected fun bindContentView(view: View) {
        binding = DataBindingUtil.bind(view)
        binding.setVariable(BR.vm, viewModel)
        viewModel.attachViewOrThrowRuntimeException(this, null)
    }

    private inline fun <reified T : Fragment> Context.getFragment(containerId: Int) =
            castWithUnwrap<FragmentActivity>()?.run { supportFragmentManager.findFragmentById(containerId) as? T }

    fun executePendingBindings() {
        binding.executePendingBindings()
    }
}