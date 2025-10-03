<template>
  <div class="home">
    <h1>Where <span style="font-family: 'Arial Black', serif">next?</span></h1>
    <p>Event Rider is the place for discovering your next event!</p>
    <p>
      <img
        alt="Landing pictures"
        src="../../public/landing-pics.png"
        style="margin: auto; max-width: 500px; width: 100%"
      />
    </p>
    <p>concerts - meetups - live shows - exhibitions - more...</p>
    <ul style="list-style-type: none; padding: 0">
      <li><i class="bi bi-calendar-week-fill"></i> Browse events</li>
      <li><i class="bi bi-heart-fill"></i> Create yours</li>
      <li><i class="bi bi-envelope-fill"></i> Get updates</li>
    </ul>
    <router-link class="btn btn-secondary" to="/events" active-class="active">
      Browse Events
    </router-link>
    <router-link
      class="btn btn-primary"
      to="/signup"
      active-class="active"
      v-if="!isLogged"
    >
      Sign Up
    </router-link>
  </div>
</template>

<script>
import { getStore } from "@/common/store";

export default {
  name: "HomeView",
  computed: {
    isLogged() {
      return getStore().state.user.logged;
    },
  },
};
</script>

<style scoped>
.home {
  padding: 20px;
}

.btn {
  margin: 10px;
}
</style>
