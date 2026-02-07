package com.example.budgetbuddy.data

import com.example.budgetbuddy.data.impl.BudgetRepositoryImpl
import com.example.budgetbuddy.data.impl.CategoryRepositoryImpl
import com.example.budgetbuddy.data.impl.TransactionRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Abstract class used to bind Repository interfaces to their corresponding implementation
 * Decouples high-level logic from low-level implementation detail per Dependency Inversion.
 *
 * As with [com.example.budgetbuddy.data.db.DBModule], this is a Singleton Module,
 * indicating to Hilt that this class acts as a "factory" or provider to the dependency tree.
 *
 * In DBModule, concrete methods are used to determine exactly how the AppDB and DAOs are created
 * However, here we can take advantage of Hilt's ability to link together implementations
 * and interfaces simply using the parameter and return type (binding), instead of boilerplate code
 * and repetitive factory methods. When the interface is referenced, Hilt will direct the execution
 * to the correct implementation.
 *
 * Using this binding greatly simplifies the process of modifying the underlying implementation
 * without needing to modify swaths of code- simply change one line here.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepoModule
{
    /**
     * Binds the TransactionRepository to the provided implementation
     */
    @Binds
    @Singleton
    abstract fun bindTransactionRepo(
        impl: TransactionRepositoryImpl
    ): TransactionRepository

    /**
     * Binds the CategoryRepository to the provided implementation
     */
    @Binds
    @Singleton
    abstract fun bindCategoryRepo(
        impl: CategoryRepositoryImpl
    ): CategoryRepository

    /**
     * Binds the BudgetRepository to the provided implementation
     */
    abstract fun bindBudgetRepo(
        impl: BudgetRepositoryImpl
    ): BudgetRepository

}