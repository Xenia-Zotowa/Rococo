<script lang="ts">
    import {Avatar, getModalStore, getToastStore} from "@skeletonlabs/skeleton";
    import FormWrapper from "$lib/components/FormWrapper.svelte";
    import Input from "$lib/components/formElements/Input.svelte";
    import {sessionStore} from "$lib/stores/sessionStore";
    import {userFormErrorStore} from "$lib/components/forms/user/user-form.error.store";
    import ImageInput from "$lib/components/formElements/ImageInput.svelte";
    import {artistsFormErrorStore} from "$lib/components/forms/artist/artist-form.error.store";
    import {validateImage} from "$lib/helpers/validate";
    import {blobToBase64} from "$lib/helpers/imageUtils";
    import {validateForm} from "$lib/components/forms/user/validate";
    import {apiClient} from "$lib/api/apiClient";
    import {getLogoutLink, idTokenFromLocalStorage} from "$lib/api/authUtils";
    import ModalButtonGroup from "$lib/components/ModalButtonGroup.svelte";

    const modalStore = getModalStore();
    const toastStore = getToastStore();

    export let parent: any;

    let files: FileList;

    // Reactive declarations to ensure we always use the latest session data
    $: id = $sessionStore.user?.id;
    $: username = $sessionStore.user?.username;
    $: firstname = $sessionStore.user?.firstname ?? "";
    $: lastname = $sessionStore.user?.lastname ?? "";
    $: avatar = $sessionStore.user?.avatar ?? "";

    // Local state for form editing to avoid mutating sessionStore directly while typing
    let editFirstname = firstname;
    let editLastname = lastname;
    let editAvatar = avatar;

    // Sync local state when session updates (e.g., after successful save)
    $: {
        editFirstname = firstname;
        editLastname = lastname;
        editAvatar = avatar;
    }

    userFormErrorStore.set({
        firstname: "",
        lastname: "",
        avatar: "",
    });

    const onSubmit = async (evt: SubmitEvent) => {
        evt.preventDefault();
        const file = files?.[0];

        if(file) {
            const validation = validateImage(file);
            if (validation) {
                artistsFormErrorStore.update((prevState) => {
                    return {
                        ...prevState,
                        photo: validation,
                    }
                });
                return;
            }
            avatar = await blobToBase64(file) as string;
            editAvatar = avatar;
        }

        validateForm(editFirstname, editLastname);

    if(!Object.values($userFormErrorStore).some(v => (v as string).length > 0) && username) {
            const res = await apiClient.updateUser({
                username,
                firstname: editFirstname,
                lastname: editLastname,
                avatar: editAvatar,
            });

            if(res.error) {
                toastStore.error(res.error);
                return;
            }

            if(res.data) {
                // The updateProfileCallback in HeaderMenu handles updating the sessionStore
                if($modalStore[0]?.response) {
                    $modalStore[0].response(res.data);
                }
                modalStore.close();
            }
        }
    }

    const onLogoutClick = async() => {
        const token = idTokenFromLocalStorage();
        modalStore.close();
        window.location.replace(getLogoutLink(token));
    }

</script>

{#if $modalStore[0] && username}
    <FormWrapper modalTitle={$modalStore[0].title ?? ""} modalBody={$modalStore[0].body ?? ""}>
        <form class="modal-form space-y-4 relative" on:submit={onSubmit}>
            <div class="text-right absolute right-0">
                <button type="button" class="btn variant-ghost" on:click={onLogoutClick}>
                    Выйти
                </button>
            </div>
            <Avatar class="mx-auto" src={editAvatar} width="w-48" rounded="rounded-full" />
            <h4 class="text-center">@{username}</h4>
            <ImageInput
                    label="Обновить фото профиля"
                    name="content"
                    bind:files={files}
                    error={$userFormErrorStore.avatar}
            />
            <Input
                    label="Имя"
                    name="firstname"
                    placeholder="Ваше имя..."
                    bind:value={editFirstname}
                    error={$userFormErrorStore.firstname}
            />
            <Input
                    label="Фамилия"
                    name="lastname"
                    placeholder="Ваша фамилия..."
                    bind:value={editLastname}
                    error={$userFormErrorStore.lastname}
            />
            <div class="text-center">
                <ModalButtonGroup onClose={parent.onClose} submitButtonText="Обновить профиль"/>
            </div>
        </form>
    </FormWrapper>
{/if}