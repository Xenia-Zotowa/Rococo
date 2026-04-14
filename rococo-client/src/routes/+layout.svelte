<script lang="ts">
	import '../app.postcss';
	import {
		AppShell,
		AppBar,
		Modal,
		initializeStores,
		Toast,
	} from '@skeletonlabs/skeleton';
	import {onMount} from "svelte";
	import {apiClient} from "$lib/api/apiClient";
	import {sessionStore} from "$lib/stores/sessionStore.js";
	import PagesNavigation from "$lib/components/PagesNavigation.svelte";
	import ToastHandler from "$lib/components/ToastHandler.svelte";
	import HeaderMenu from "$lib/components/HeaderMenu.svelte";
	import MainTitle from "$lib/components/MainTitle.svelte";
	import {goto} from "$app/navigation";

	initializeStores();
	let isMenuVisible = false;

	const checkCurrentUrlAndRedirectToPreConfiguredUrl = async (currentUrl: URL) => {
		const preconfiguredHost = import.meta.env.VITE_FRONT_HOST;
		if (preconfiguredHost && currentUrl.host !== preconfiguredHost){
			currentUrl.host = preconfiguredHost;
			currentUrl.hostname = preconfiguredHost;
			await goto(currentUrl.toString());
		}
	}

	onMount(async () => {
		console.log("onMount: starting");
		const currentUrl = new URL(window.location.href);

		await checkCurrentUrlAndRedirectToPreConfiguredUrl(currentUrl);

		if (currentUrl.pathname === "/authorized" || currentUrl.pathname === "/logout") {
			console.log("onMount: skip session check (auth/logout route)");
			return;
		}

		console.log("onMount: triggering loadSession");
		sessionStore.update((prevState) => {
			return {
				...prevState,
				isLoading: true,
			}
		});

		const res = await apiClient.loadSession();
		console.log("onMount: loadSession result", res);

		if(res.error) {
			console.warn("Can not load Session", res.error);
			sessionStore.update((prevState) => {
				return {
					...prevState,
					isLoading: false,
				}
			});
		} else {
			if (!!res.data?.username && res.data?.username !== $sessionStore.user?.username) {
				console.log("onMount: user mismatch, loading full user data");
				const userRes = await apiClient.loadUser();
				console.log("onMount: loadUser result", userRes);
				if (userRes.data) {
					sessionStore.update((prevState) => {
						return {
							...prevState,
							user: userRes.data,
						}
					});
				}
			}
			sessionStore.update((prevState) => {
				return {
					...prevState,
					isLoading: false,
				}
			});
		}
		console.log("onMount: finished");
	});

	const toggleMenu = () => {
		isMenuVisible = !isMenuVisible;
	}

</script>

<Modal />
<AppShell>
	<svelte:fragment slot="header">
		<AppBar gridColumns="grid-cols-3" slotDefault="place-self-center" slotTrail="place-content-end" class="px-6">
			<svelte:fragment slot="lead">
				<MainTitle/>
			</svelte:fragment>
			<svelte:fragment slot="trail">
				<ToastHandler let:triggerError let:triggerSuccess>
					<HeaderMenu
							{toggleMenu}
							errorTrigger={triggerError}
							successTrigger={triggerSuccess}
					/>
				</ToastHandler>
			</svelte:fragment>
		</AppBar>
		{#if isMenuVisible}
			<PagesNavigation isBigScreen={false}/>
		{/if}
	</svelte:fragment>
	<slot/>
</AppShell>
<Toast />