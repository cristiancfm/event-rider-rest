<template>
  <div class="container-fluid text-start p-4">
    <div class="row">
      <div class="col-sm-3 mb-3" style="max-width: 130px">
        <div style="aspect-ratio: 1/1">
          <img
            :src="getImageSrc()"
            class="d-block w-100"
            style="object-fit: cover; width: 100%; height: 100%"
            alt="Profile image"
            @error="setPlaceholder"
          />
        </div>
      </div>
      <div class="col-sm-9">
        <h3 style="text-transform: none">
          {{ user.name }} {{ user.surname }}
          <span class="ms-1 me-2" v-if="user.authority === 'USER_VERIFIED'">
            <i class="bi bi-patch-check-fill"></i>
          </span>
          <span class="ms-1 me-2" v-if="user.authority === 'ADMIN'">
            <i class="bi bi-person-badge-fill"></i>
          </span>
          <!-- Edit profile button -->
          <router-link
            class="btn btn-secondary m-1"
            to="/profile/edit"
            v-if="isLogged"
          >
            <i class="bi bi-pencil-fill"></i> Edit profile
          </router-link>
          <!-- **** -->
        </h3>
        <div class="row" v-if="user.biography">
          <p>{{ user.biography }}</p>
        </div>
        <div class="row">
          <div class="col">
            <p class="text-secondary">
              {{ user.upcomingHostedEvents.length }}
              <span v-if="user.upcomingHostedEvents.length === 1">
                upcoming event
              </span>
              <span v-else>upcoming events</span>
              |
              {{ user.hostedEvents.length }}
              <span v-if="user.hostedEvents.lenght === 1">total event</span>
              <span v-else>total events</span>
            </p>
            <p>
              <router-link to="/profile/followers">
                {{ user.followers.length }}
                <span v-if="user.followers.length === 1">follower</span>
                <span v-else>followers</span>
              </router-link>
              |
              <router-link to="/profile/following">
                {{ user.following.length }} following
              </router-link>
            </p>
          </div>
        </div>
      </div>
    </div>
    <br />
    <div class="row">
      <div class="container-fluid text-center">
        <router-link
          class="btn btn-secondary me-2 mb-2"
          to="/profile/hosted-events"
          active-class="active"
        >
          Hosted Events
        </router-link>
        <router-link
          class="btn btn-secondary me-2 mb-2"
          to="/profile/saved-events"
          active-class="active"
        >
          Saved Events
        </router-link>
        <router-link
          class="btn btn-secondary me-2 mb-2"
          to="/profile/subscribed-events"
          active-class="active"
        >
          Subscribed Events
        </router-link>
        <router-link
          class="btn btn-secondary me-2 mb-2"
          to="/profile/subscribed-categories"
          active-class="active"
        >
          Subscribed Categories
        </router-link>
      </div>
    </div>
    <br />
    <router-view :user="this.user" />
  </div>
</template>

<script>
import { BACKEND_URL } from "@/constants";
import { getStore } from "@/common/store";

export default {
  name: "ProfileDetail",
  props: {
    user: {
      type: Object,
      required: true,
    },
  },
  methods: {
    getImageSrc() {
      if (this.user.image) {
        return `${BACKEND_URL}/users/${this.user.id}/image`;
      }
      return "/profile-placeholder.jpg";
    },
    setPlaceholder(event) {
      event.target.src = "/error-placeholder-square.png";
    },
  },
  computed: {
    isLogged() {
      return getStore().state.user.logged;
    },
  },
};
</script>

<style scoped>
body {
  width: 1280px;
  margin: auto;
}
</style>
