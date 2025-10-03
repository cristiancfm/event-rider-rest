<template>
  <div class="text-start p-2">
    <h2 class="m-2" v-if="this.title" v-html="title"></h2>
    <EventFilters ref="eventFilters" @filters-applied="applyFilters" />
    <div class="row">
      <div class="col-md-8 pe-md-0">
        <div class="d-flex flex-wrap justify-content-start">
          <div
            class="col-sm-12 col-md-6"
            v-for="event in events"
            :key="event.id"
          >
            <EventCard
              :event="event"
              :edit-events="editEvents"
              @subscribers="updateSubscribers"
              @saves="updateSaves"
              @show-in-map="showEventInMap"
            ></EventCard>
          </div>
        </div>
      </div>
      <div class="col-md-4 order-first order-md-last">
        <EventMap
          class="m-2"
          v-if="events.length > 0"
          :events="events"
          :latitude="this.latitude"
          :longitude="this.longitude"
          :zoom="13"
          :show-in-map-event="showInMapEvent"
          @popup-closed="popupClosed"
        ></EventMap>
      </div>
    </div>
    <div v-if="events.length === 0" class="text-center mt-2">
      <img
        src="../../../public/event-rider-logo-alt.png"
        style="opacity: 10%; filter: grayscale(100%)"
        alt=""
      />
      <h2>No Events</h2>
      <br />
    </div>
  </div>
</template>

<script>
import EventFilters from "@/components/events/EventFilters.vue";
import EventCard from "@/components/events/EventCard.vue";
import { updateSubscribers, updateSaves } from "@/common/event";
import EventMap from "@/components/events/EventMap.vue";

export default {
  name: "EventsView",
  props: {
    title: {
      // the title to show in the component
      type: String,
      required: true,
    },
    getEvents: {
      // the function to retrieve the events
      type: Function,
      required: true,
    },
    editEvents: {
      // whether to show edit button or not
      type: Boolean,
      required: false,
    },
  },
  data() {
    return {
      events: [],
      latitude: null,
      longitude: null,
      permanentCategory: null,
      showInMapEvent: null,
    };
  },
  components: { EventFilters, EventCard, EventMap },
  methods: {
    async applyFilters(filters) {
      let query = [];
      filters.title ? query.push({ name: "title", value: filters.title }) : "";
      if (filters.latitude) {
        query.push({ name: "latitude", value: filters.latitude });
        this.latitude = filters.latitude;
      } else {
        this.latitude = null;
      }
      if (filters.longitude) {
        query.push({ name: "longitude", value: filters.longitude });
        this.longitude = filters.longitude;
      } else {
        this.longitude = null;
      }
      filters.date ? query.push({ name: "date", value: filters.date }) : "";
      filters.distance
        ? query.push({ name: "distance", value: filters.distance })
        : "";
      filters.category
        ? query.push({ name: "category", value: filters.category })
        : "";
      filters.cancelled
        ? query.push({ name: "cancelled", value: filters.cancelled })
        : "";
      this.events = await this.getEvents(query, null);
    },
    updateSubscribers,
    updateSaves,
    showEventInMap(event) {
      this.showInMapEvent = event;
    },
    popupClosed() {
      this.showInMapEvent = null;
    },
  },
  created() {
    // Set permanent category if the URL is /event-categories/:id
    if (this.$route.path.startsWith("/event-categories/")) {
      if (this.$route.params.id) {
        this.permanentCategory = this.$route.params.id;
      }
    }
  },
  async mounted() {
    this.events = await this.getEvents();
    if (this.permanentCategory) {
      let query = [];
      query.push({ name: "category", value: this.permanentCategory });
      this.events = await this.getEvents(query, null);
    }
  },
};
</script>

<style scoped></style>
