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
              {{ user.followers.length }}
              <span v-if="user.followers.length === 1">follower</span>
              <span v-else>followers</span>
              |
              {{ user.following.length }} following
            </p>
          </div>
        </div>
      </div>
    </div>
    <br />
    <div class="row">
      <ul class="nav nav-tabs" id="events-nav" role="tablist">
        <li class="nav-item" role="presentation">
          <button
            class="nav-link active"
            id="upcoming-events-tab"
            data-bs-toggle="tab"
            data-bs-target="#upcoming-events"
            type="button"
            role="tab"
            aria-controls="upcoming events"
            aria-selected="true"
          >
            Upcoming
          </button>
        </li>
        <li class="nav-item" role="presentation">
          <button
            class="nav-link"
            id="past-events-tab"
            data-bs-toggle="tab"
            data-bs-target="#past-events"
            type="button"
            role="tab"
            aria-controls="past events"
            aria-selected="false"
          >
            Past
          </button>
        </li>
      </ul>
      <div class="tab-content" id="events-nav-content">
        <div
          class="tab-pane fade show active"
          id="upcoming-events"
          role="tabpanel"
          aria-labelledby="upcoming-events-tab"
        >
          <EventsList :title="''" :get-events="getUpcomingEvents" />
        </div>
        <div
          class="tab-pane fade"
          id="past-events"
          role="tabpanel"
          aria-labelledby="past-events-tab"
        >
          <EventsList :title="''" :get-events="getPastEvents" />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { BACKEND_URL } from "@/constants";
import { getStore } from "@/common/store";
import UserRepository from "@/repositories/UserRepository";
import EventsList from "@/components/events/EventsList";

export default {
  name: "UserDetail",
  components: { EventsList },
  props: {
    user: {
      type: Object,
      required: true,
    },
  },
  data() {
    return {
      isFollowed: false,
    };
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
    async followUser() {
      this.$emit("followers", this.user);
    },
    async getUpcomingEvents(query) {
      let events = await UserRepository.findUserUpcomingEvents(
        this.user.id,
        query,
        null
      );
      // Cancelled events will not be shown
      events = events.filter((event) => event.status === "PUBLISHED");
      return events;
    },
    async getPastEvents(query) {
      let events = await UserRepository.findUserPastEvents(
        this.user.id,
        query,
        null
      );
      // Cancelled events will not be shown
      events = events.filter((event) => event.status === "PUBLISHED");
      return events;
    },
    handleTabChange() {
      // Leaflet will not render correctly when the user changes between tabs
      // because the containers are hidden and shown and Leaflet is not aware
      // of that. Resizing the browser window will fix the issue, since Leaflet
      // listens to the browser resizing event.
      window.dispatchEvent(new Event("resize"));
    },
  },
  mounted() {
    const eventsNav = document.querySelector("#events-nav");
    eventsNav.addEventListener("shown.bs.tab", this.handleTabChange);
  },
  computed: {
    isLogged() {
      return getStore().state.user.logged;
    },
  },
  watch: {
    "user.followers": {
      handler: async function (newFollowers) {
        if (this.isLogged) {
          const account = await UserRepository.findOneBase(
            getStore().state.user.id
          );
          const index = newFollowers.findIndex(
            (follower) => follower.id === account.id
          );
          this.isFollowed = index >= 0;
        }
      },
      immediate: true,
      deep: true,
    },
  },
};
</script>

<style scoped></style>
