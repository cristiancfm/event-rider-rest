<template>
  <div class="row">
    <h2>
      <i class="bi bi-bookmark-fill"></i>
      Saved
      <span style="font-family: Arial Black, serif">Events</span>
    </h2>
  </div>
  <div class="row ps-3 pe-3">
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
          aria-selected="false"
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
    <div
      class="tab-content p-0 border-start border-end border-bottom"
      id="events-nav-content"
      style="border-radius: 0 0 5px 5px"
    >
      <div
        class="tab-pane fade show active"
        id="upcoming-events"
        role="tabpanel"
        aria-labelledby="upcoming-events-tab"
      >
        <EventsList :title="''" :get-events="getSavedUpcomingEvents" />
      </div>
      <div
        class="tab-pane fade"
        id="past-events"
        role="tabpanel"
        aria-labelledby="past-events-tab"
      >
        <EventsList :title="''" :get-events="getSavedPastEvents" />
      </div>
    </div>
  </div>
</template>

<script>
import EventsList from "@/components/events/EventsList.vue";
import UserRepository from "@/repositories/UserRepository";
import { getStore } from "@/common/store";

export default {
  name: "ProfileSavedEvents",
  components: { EventsList },
  props: {
    user: {
      type: Object,
      required: true,
    },
  },
  methods: {
    async getSavedUpcomingEvents(query) {
      return await UserRepository.findUserSavedUpcomingEvents(
        getStore().state.user.id,
        query,
        null
      );
    },
    async getSavedPastEvents(query) {
      return await UserRepository.findUserSavedPastEvents(
        getStore().state.user.id,
        query,
        null
      );
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
};
</script>

<style scoped></style>
