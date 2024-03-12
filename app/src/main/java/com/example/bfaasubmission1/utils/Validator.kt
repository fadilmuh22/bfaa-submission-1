package com.example.bfaasubmission1.utils

class Validator {
    companion object {
        fun isSearchTextValid(searchText: String): Boolean {
            return searchText.isNotEmpty() && searchText.length > 3
        }
    }
}
