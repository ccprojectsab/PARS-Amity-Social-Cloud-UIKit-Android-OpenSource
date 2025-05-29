package com.amity.socialcloud.uikit.community.home.fragments

import android.R.attr.bottom
import android.R.attr.left
import android.R.attr.right
import android.R.attr.top
import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.amity.socialcloud.uikit.common.base.AmityFragmentStateAdapter
import com.amity.socialcloud.uikit.common.common.views.AmityColorPaletteUtil
import com.amity.socialcloud.uikit.common.common.views.AmityColorShade
import com.amity.socialcloud.uikit.common.components.AmityToolBarClickListener
import com.amity.socialcloud.uikit.common.model.AmityEventIdentifier
import com.amity.socialcloud.uikit.common.utils.AmityAndroidUtil
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.databinding.AmityFragmentCommunityHomePageBinding
import com.amity.socialcloud.uikit.community.explore.fragments.AmityCommunityExplorerFragment
import com.amity.socialcloud.uikit.community.newsfeed.fragment.AmityNewsFeedFragment
import com.amity.socialcloud.uikit.community.search.AmityUserSearchFragment
import com.amity.socialcloud.uikit.community.setting.AmityCommunitySearchFragment
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.concurrent.TimeUnit


class AmityCommunityHomePageFragment : Fragment(), AmityToolBarClickListener {
    interface TabSelectionListener {
        fun onTabSelected(tabIndex: Int)
    }

    private lateinit var fragmentStateAdapter: AmityFragmentStateAdapter
    private lateinit var globalSearchStateAdapter: AmityFragmentStateAdapter
    private lateinit var searchMenuItem: MenuItem
    private lateinit var binding: AmityFragmentCommunityHomePageBinding
    private val viewModel: AmityCommunityHomeViewModel by activityViewModels()
    private var textChangeDisposable: Disposable? = null
    private val textChangeSubject: PublishSubject<String> = PublishSubject.create()
    private val searchString = ObservableField("")
    private var selectedTab = SelectedTab.NEWS_FEED
    var tabSelectionListener: TabSelectionListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("MyTag", "On Create - $selectedTab")
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.amity_fragment_community_home_page,
            container,
            false
        )
        binding.viewModel = viewModel

        binding.tabLayout.disableSwipe()
        binding.tabLayout.setOffscreenPageLimit(2)
        initTabLayout()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        setUpSearchTabLayout()
        addViewModelListeners()
        subscribeTextChangeEvents()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (textChangeDisposable?.isDisposed == false) {
            textChangeDisposable?.dispose()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is TabSelectionListener) {
            tabSelectionListener = context
        } else {
            throw RuntimeException("$context must implement TabSelectionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        tabSelectionListener = null
    }

    fun switchTab(tab: SelectedTab) {
        selectedTab = tab
        Log.d("MyTag", "switchTab $selectedTab")
        if (this::binding.isInitialized) {
            Log.d("MyTag", "is intialized $selectedTab")
            if (tab == SelectedTab.EXPLORE) {
                binding.tabLayout.switchTab(1, false)
            } else {
                binding.tabLayout.switchTab(0, false)
            }
        }
    }

    private fun initTabLayout() {

        fragmentStateAdapter = AmityFragmentStateAdapter(
            childFragmentManager,
            requireActivity().lifecycle
        )
        globalSearchStateAdapter = AmityFragmentStateAdapter(
            childFragmentManager,
            requireActivity().lifecycle
        )
        fragmentStateAdapter.setFragmentList(
            arrayListOf(
                AmityFragmentStateAdapter.AmityPagerModel(
                    getString(R.string.amity_title_news_feed),
                    getNewsFeedFragment()
                ),
                AmityFragmentStateAdapter.AmityPagerModel(
                    getString(R.string.amity_title_explore),
                    getExploreFragment()
                )
            )
        )
        binding.tabLayout.setAdapter(fragmentStateAdapter)
        Log.d("MyTag", "tabLayout$selectedTab")
        if (selectedTab == SelectedTab.EXPLORE) {
            binding.tabLayout.switchTab(1, false)
        } else {
            binding.tabLayout.switchTab(0, false)
        }

        binding.tabLayout.setPageChangeListener(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                (activity as? TabSelectionListener)?.onTabSelected(position)
                //  fragmentStateAdapter.notifyItemChanged(position)
                //   currentSelectedPosition = position

                super.onPageSelected(position)
                //  currentSelectedPosition = position
            }
        })

        /*   binding.tabLayout.setPageChangeListener(object : ViewPager2.OnPageChangeCallback() {
               override fun onPageSelected(position: Int) {
                   Log.d("PAGE_CHANGED", "page changed---$position")
                   fragmentStateAdapter.notifyItemChanged(position)
                   super.onPageSelected(position)
               }
           })*/
    }


    private fun getExploreFragment(): Fragment {
        return AmityCommunityExplorerFragment.newInstance().build()
    }

    private fun getNewsFeedFragment(): Fragment {
        return AmityNewsFeedFragment.newInstance().build()
    }

    private fun addViewModelListeners() {
        viewModel.onAmityEventReceived += { event ->
            Log.d("MyTag", "eventListerners${event.type}")
            when (event.type) {

                AmityEventIdentifier.EXPLORE_COMMUNITY -> {
                    //searchMenuItem.expandActionView()
                    binding.tabLayout.switchTab(1)
                }

                else -> {
//                    binding.tabLayout.switchTab(0)
                }
            }
        }
    }

    private fun initToolbar() {

        binding.communityHomeToolbar.setLeftString(getString(R.string.amity_community))
        (activity as AppCompatActivity).supportActionBar?.displayOptions =
            ActionBar.DISPLAY_SHOW_CUSTOM
        (activity as AppCompatActivity).setSupportActionBar(binding.communityHomeToolbar as Toolbar)
        setHasOptionsMenu(true)
    }

    private fun setUpSearchTabLayout() {
        globalSearchStateAdapter.setFragmentList(
            arrayListOf(
                AmityFragmentStateAdapter.AmityPagerModel(
                    getString(R.string.amity_communities),
                    AmityCommunitySearchFragment.newInstance(searchString)
                        .build(requireActivity() as AppCompatActivity)
                ),
                AmityFragmentStateAdapter.AmityPagerModel(
                    getString(R.string.amity_accounts),
                    AmityUserSearchFragment.newInstance(searchString)
                        .build(requireActivity() as AppCompatActivity)
                )
            )
        )
        binding.globalSearchTabLayout.setAdapter(globalSearchStateAdapter)
    }

    private fun subscribeTextChangeEvents() {
        textChangeDisposable = textChangeSubject.debounce(300, TimeUnit.MILLISECONDS)
            .map {
                if (searchString.get() != it) {
                    searchString.set(it)
                }
                //viewModel.emptySearchString.set(it.isEmpty())
            }
            .subscribe()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        val searchManager =
            requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView =
            SearchView((activity as AppCompatActivity).supportActionBar!!.themedContext)
        searchView.queryHint = getString(com.amity.socialcloud.uikit.common.R.string.amity_search)
        searchView.maxWidth = Int.MAX_VALUE
        searchView.setBackgroundResource(R.drawable.amity_search_bg_selector)


        val searchEditText =
            searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
        searchEditText.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                com.amity.socialcloud.uikit.common.R.color.amityColorBase
            )
        )
        searchEditText.setHintTextColor(
            AmityColorPaletteUtil.getColor(
                ContextCompat.getColor(
                    requireContext(),
                    com.amity.socialcloud.uikit.common.R.color.amityColorBase
                ),
                AmityColorShade.SHADE2
            )
        )
        searchEditText.imeOptions = EditorInfo.IME_ACTION_NEXT
        searchEditText.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    AmityAndroidUtil.hideKeyboard(searchEditText)
                    return true
                }
                return false
            }
        })

        searchMenuItem = menu.add("SearchMenu").setVisible(true).setActionView(searchView)
            .setIcon(com.amity.socialcloud.uikit.common.R.drawable.amity_ic_search)
        searchMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM or MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW)
        searchView.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
        val queryTextListener = object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { textChangeSubject.onNext(it) }
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { textChangeSubject.onNext(it) }
                searchMenuItem.collapseActionView()
                return true
            }
        }

        searchView.setOnQueryTextListener(queryTextListener)

        searchMenuItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                viewModel.isSearchMode.set(true)
                viewModel.emptySearchString.set(false)
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                viewModel.isSearchMode.set(false)
                viewModel.emptySearchString.set(false)
                return true
            }
        })

    }

    class Builder() {

        fun build(): AmityCommunityHomePageFragment {

            return AmityCommunityHomePageFragment().apply {

            }

        }
    }
    /*
        class Builder internal constructor() {
            fun build(): AmityCommunityHomePageFragment {
                return AmityCommunityHomePageFragment()
            }
        }*/

    companion object {

        enum class SelectedTab {
            EXPLORE,
            NEWS_FEED
        }

        fun newInstance(): Builder {
            return Builder()
        }
    }

    override fun leftIconClick() {
        activity?.onBackPressed()
    }

    override fun rightIconClick() {

    }
}