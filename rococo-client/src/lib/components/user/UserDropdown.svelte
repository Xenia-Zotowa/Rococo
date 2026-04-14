<script lang="ts">
    import { sessionStore } from "$lib/stores/sessionStore";
    import { Avatar } from "@skeletonlabs/skeleton";
    import { getModalStore } from "@skeletonlabs/skeleton";
    import UserForm from "$lib/components/forms/user/UserForm.svelte";
    import { prepareModal } from "$lib/helpers/prepareModal";
    import { getLogoutLink, idTokenFromLocalStorage } from "$lib/api/authUtils";

    export let errorTrigger: (message: string) => void;
    export let successTrigger: (message: string) => void;

    const modalStore = getModalStore();

    const updateProfileCallback = async (result: { data?: any, error?: string }) => {
        if(result.error) {
            errorTrigger(result.error);
            return;
        }
        if(result.data) {
            sessionStore.update(s => ({ ...s, user: result.data }));
            successTrigger("Профиль обновлен");
        }
    }

    const openEditModal = () => {
        const modal = prepareModal({
            ref: UserForm,
            title: "Профиль",
            callback: updateProfileCallback,
        });
        modalStore.trigger(modal);
    }

    const handleLogout = () => {
        const token = idTokenFromLocalStorage();
        window.location.replace(getLogoutLink(token));
    }
</script>

<div class="absolute right-0 mt-2 w-56 bg-surface-100-800-token shadow-xl rounded-lg py-2 z-[100] border border-surface-500/30">
    <div class="px-4 py-2 border-b border-surface-500/20 mb-1">
        {#if $sessionStore.user}
            <p class="text-sm font-bold truncate">{$sessionStore.user.username}</p>
            <p class="text-xs text-surface-400">Пользователь</p>
        {/if}
    </div>

    <button on:click={openEditModal} class="w-full text-left px-4 py-2 text-sm hover:bg-surface-500/10 transition-colors">
        Редактировать профиль
    </button>

    <button on:click={handleLogout} class="w-full text-left px-4 py-2 text-sm text-error-500 hover:bg-error-500/10 transition-colors">
        Выйти
    </button>
</div>