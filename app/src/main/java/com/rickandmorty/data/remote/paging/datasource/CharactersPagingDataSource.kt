package com.rickandmorty.data.remote.paging.datasource

import android.net.Uri
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.rickandmorty.data.remote.api.CharacterApi
import com.rickandmorty.data.remote.model.CharacterData
import com.rickandmorty.data.remote.model.CharacterList

class CharactersPagingDataSource(private val characterApi: CharacterApi, val query: Map<String, String>) :
    PagingSource<Int, CharacterData>() {


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CharacterData> {
        return try {
            val response : CharacterList
            val nextPage : Int = params.key ?: FIRST_PAGE_INDEX
            if(query.isEmpty()) {
                response  = characterApi.getCharacters(nextPage)
            } else {
                response = characterApi.filterCharacters(nextPage, query)

            }
            var nextPageNumber : Int? = null

            if (response.info.next != null){
                val uri = Uri.parse(response.info.next)
                val nextPageQuery = uri.getQueryParameter("page")
                nextPageNumber = nextPageQuery?.toInt()
            }

            LoadResult.Page(
                data = response.results,
                prevKey = null,
                nextKey = nextPageNumber
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, CharacterData>): Int? {
        return state.anchorPosition

    }

    companion object{
        private const val FIRST_PAGE_INDEX = 1
    }
}