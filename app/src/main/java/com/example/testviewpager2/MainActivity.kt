package com.example.testviewpager2

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.doOnNextLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2

internal class MainActivity : AppCompatActivity() {

    private val pager: ViewPager2
        get() = findViewById(R.id.pager)

    lateinit var cardAdapter: CardAdapter
    var counter = 1

    private val deque: ArrayDeque<List<TestData>> = ArrayDeque()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initPager()
        initButtons()
    }

    private fun initPager() {
        cardAdapter = CardAdapter()
        cardAdapter.items = createList().also(deque::add)

        with(pager) {
            adapter = cardAdapter
            offscreenPageLimit = 3
            setPageTransformer(
                CardPageTransformer(
                    cardWidth = resources.getDimensionPixelSize(R.dimen.card_details_card_width),
                    distanceBetweenCards = resources.getDimensionPixelSize(R.dimen.card_details_distance_between_cards)
                )
            )
            cardAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver(){

                override fun onChanged() {
                    // todo: transform not called on each data change. Only work correct with notifyDataSetChanged
                    pager.doOnNextLayout { pager.requestTransform() }
                }

                override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                    onChanged()
                }

                override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
                    onChanged()
                }

                override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                    onChanged()
                }

                override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                    onChanged()
                }

                override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                    onChanged()
                }
            })
        }
    }

    private fun initButtons() {
        findViewById<Button>(R.id.deleteCardButton).setOnClickListener {
            cardAdapter.items
                .getOrNull(pager.currentItem)
                ?.let(::onDeleteClicked)

        }
        findViewById<Button>(R.id.revertButton).setOnClickListener {
            onRevertClicked()
        }

        findViewById<Button>(R.id.addButton).setOnClickListener {
            addCard()
        }
    }

    private fun createList() = listOf(
        createItem(),
        createItem(),
        createItem(),
    )

    private fun addCard() {
        cardAdapter.items = cardAdapter.items + createItem()
    }

    private fun createItem(): TestData {
        val (name, color) = getColorAndName(counter++)
        return TestData(name, color)
    }

    private fun getColorAndName(counter: Int): Pair<String, Int> {
        return counter.toString() to getColorFromCounter(counter)
    }

    private fun getColorFromCounter(counter: Int): Int {
        val colors = listOf(
            getColor(R.color.teal_200),
            getColor(R.color.purple_500),
            getColor(R.color.teal_700),
        )

        return colors[counter % colors.size]
    }

    private fun onDeleteClicked(testData: TestData) {
        val oldItems = cardAdapter.items.also(deque::add)
        val newItems = oldItems.filter { it != testData }

        cardAdapter.items = newItems
        pager.currentItem = oldItems.indexOf(testData) - 1
    }

    private fun onRevertClicked() {
        deque.removeLastOrNull()?.also {
            cardAdapter.items = it
        }
    }


}
