package ru.surfstudio.android.core.ui.navigation.feature.route.feature

/**
 * Marker interface for navigation between fragments in different feature-modules.
 * This interface is using for ProGuard settings to avoid obfuscation of names of these fragments,
 * otherwise navigation will not work correctly.
 */
interface CrossFeatureFragment