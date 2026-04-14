import type { LayoutLoad } from './$types';
import { apiClient } from '$lib/api/apiClient';
import { sessionStore } from '$lib/stores/sessionStore';
import { goto } from '$app/navigation';

export const ssr = false;
export const prerender = false;

export const load: LayoutLoad = async ({ url }) => {
    const currentUrl = new URL(url.href);

    // Redirect if hostname mismatch
    const preconfiguredHost = import.meta.env.VITE_FRONT_HOST;
    if (preconfiguredHost && currentUrl.hostname !== preconfiguredHost){
        currentUrl.hostname = preconfiguredHost;
        await goto(currentUrl.toString());
    }

    // Skip session check for specific routes
    if (currentUrl.pathname === "/authorized" || currentUrl.pathname === "/logout") {
        return {};
    }

    // Start loading state
    sessionStore.update(s => ({ ...s, isLoading: true }));

    try {
        const res = await apiClient.loadSession();

        if (res.error) {
            console.warn("Can not load Session", res.error);
            sessionStore.update(s => ({ ...s, isLoading: false }));
        } else {
            if (res.data?.username) {
                const userRes = await apiClient.loadUser();
                if (userRes.data) {
                    sessionStore.update(s => ({
                        ...s,
                        user: userRes.data,
                        isLoading: false
                    }));
                } else {
                    sessionStore.update(s => ({ ...s, isLoading: false }));
                }
            } else {
                sessionStore.update(s => ({ ...s, isLoading: false }));
            }
        }
    } catch (e) {
        console.error("Session load error:", e);
        sessionStore.update(s => ({ ...s, isLoading: false }));
    }

    return {};
};