<template>
  <div class="container-fluid text-start p-4">
    <div class="row">
      <h3 style="text-transform: none">{{ event.title }}</h3>
      <p>
        <router-link :to="'/members/' + event.host.id">
          {{ event.host.name
          }}{{ event.host.surname ? " " + event.host.surname : "" }}
        </router-link>
        <span class="ms-2" v-if="event.host.authority === 'USER_VERIFIED'">
          <i class="bi bi-patch-check-fill"></i>
        </span>
        <span class="ms-2" v-if="event.host.authority === 'ADMIN'">
          <i class="bi bi-person-badge-fill"></i>
        </span>
      </p>
    </div>
    <div class="row mt-2">
      <div class="col-md me-2 mb-2">
        <div id="event-carousel-indicators" class="carousel slide">
          <div class="carousel-inner">
            <template v-for="item in event.numImages" :key="item">
              <div v-if="item - 1 === 0" class="carousel-item active">
                <img
                  :src="getImageSrc(item - 1)"
                  class="d-block w-100"
                  style="
                    object-fit: cover;
                    object-position: center;
                    aspect-ratio: 3/2;
                  "
                  alt="Event image"
                  @error="setPlaceholder"
                />
              </div>
              <div v-if="item - 1 !== 0" class="carousel-item">
                <img
                  :src="getImageSrc(item - 1)"
                  class="d-block w-100"
                  style="
                    object-fit: cover;
                    object-position: center;
                    aspect-ratio: 3/2;
                  "
                  alt="Event image"
                  @error="setPlaceholder"
                />
              </div>
            </template>
            <template v-if="event.numImages === 0">
              <div class="carousel-item active">
                <img
                  src="/event-placeholder.png"
                  class="d-block w-100"
                  style="
                    object-fit: cover;
                    object-position: center;
                    aspect-ratio: 3/2;
                  "
                  alt="Event image"
                />
              </div>
            </template>
          </div>
          <div v-if="event.numImages > 1">
            <button
              class="carousel-control-prev"
              type="button"
              data-bs-target="#event-carousel-indicators"
              data-bs-slide="prev"
            >
              <span
                class="carousel-control-prev-icon"
                aria-hidden="true"
              ></span>
              <span class="visually-hidden">Previous</span>
            </button>
            <button
              class="carousel-control-next"
              type="button"
              data-bs-target="#event-carousel-indicators"
              data-bs-slide="next"
            >
              <span
                class="carousel-control-next-icon"
                aria-hidden="true"
              ></span>
              <span class="visually-hidden">Next</span>
            </button>
          </div>
        </div>
      </div>
      <div class="col-md">
        <div
          class="row p-3"
          style="border: 1px gainsboro solid; border-radius: 5px"
        >
          <h4>Details</h4>
          <div class="col-6 col-lg-8">
            <p class="text-secondary">
              <i class="bi bi-calendar-week-fill"></i>&nbsp;
              {{ new Date(event.startingDate).toLocaleString() }}
              -
              {{ new Date(event.endingDate).toLocaleString() }}
            </p>
            <p class="text-secondary">
              <i class="bi bi-ticket-perforated-fill"></i>&nbsp;
              {{ event.category.name }}
            </p>
            <p class="text-secondary">
              <i class="bi bi-geo-alt-fill"></i>&nbsp;
              {{ eventAddress }}
            </p>
            <p class="text-secondary" v-if="event.locationDetails">
              {{ event.locationDetails }}
            </p>
          </div>
          <div class="col-6 col-lg-4">
            <!-- Save button -->
            <button
              class="btn btn-secondary m-1"
              @click="saveEvent"
              v-if="isLogged && !isSaved"
            >
              <i class="bi bi-bookmark"></i> Save
            </button>
            <button
              class="btn btn-secondary m-1"
              @click="saveEvent"
              v-if="isLogged && isSaved"
            >
              <i class="bi bi-bookmark-fill"></i> Saved
            </button>
            <router-link
              class="btn btn-secondary m-1"
              to="/login"
              active-class="active"
              v-if="!isLogged"
            >
              <i class="bi bi-bookmark"></i> Save
            </router-link>
            <!-- **** -->
            <p>
              {{ event.saves.length }}
              <span v-if="event.saves.length === 1">save</span>
              <span v-else>saves</span>
            </p>
            <!-- Subscribe button -->
            <button
              class="btn btn-secondary m-1"
              @click="subscribeToEvent"
              v-if="isLogged && !isSubscribed"
            >
              <i class="bi bi-star"></i> Subscribe
            </button>
            <button
              class="btn btn-secondary m-1"
              @click="subscribeToEvent"
              v-if="isLogged && isSubscribed"
            >
              <i class="bi bi-star-fill"></i> Subscribed
            </button>
            <router-link
              class="btn btn-secondary m-1"
              to="/login"
              active-class="active"
              v-if="!isLogged"
            >
              <i class="bi bi-star"></i> Subscribe
            </router-link>
            <!-- **** -->
            <p>
              {{ event.subscribers.length }}
              <span v-if="event.subscribers.length === 1">subscriber</span>
              <span v-else>subscribers</span>
            </p>
          </div>
        </div>
      </div>
    </div>
    <div class="row mt-2">
      <div class="col-md me-2 mb-2">
        <h4>Description</h4>
        <p>{{ event.description }}</p>
      </div>
      <div class="col-md">
        <h4>Map</h4>
        <EventMap
          :events="[event]"
          :latitude="event.latitude"
          :longitude="event.longitude"
          :zoom="13"
          :height="300"
        ></EventMap>
      </div>
    </div>
  </div>
</template>

<script>
import { BACKEND_URL, MAPBOX_TOKEN } from "@/constants";
import { getStore } from "@/common/store";
import UserRepository from "@/repositories/UserRepository";
import { MapBoxProvider } from "leaflet-geosearch";
import EventMap from "@/components/events/EventMap";

export default {
  name: "EventDetails",
  components: { EventMap },
  props: {
    event: {
      type: Object,
      required: true,
    },
  },
  data() {
    return {
      eventAddress: "",
      isSubscribed: false,
      isSaved: false,
    };
  },
  methods: {
    getImageSrc(item) {
      if (this.event.numImages > 0) {
        return `${BACKEND_URL}/events/${this.event.id}/image/${item}`;
      }
      return "/event-placeholder.png";
    },
    setPlaceholder(event) {
      event.target.src = "/error-placeholder.png";
    },
    async subscribeToEvent() {
      this.$emit("subscribers", this.event);
    },
    async saveEvent() {
      this.$emit("saves", this.event);
    },
    async coordinatesToLocation() {
      const provider = new MapBoxProvider({
        params: {
          access_token: MAPBOX_TOKEN,
        },
      });
      const results = await provider.search({
        query: this.event.coordinateY + "," + this.event.coordinateX,
        //the mapbox url uses {longitude, latitude} in that order
      });
      //take address from first result
      this.eventAddress = results[0].label;
    },
  },
  computed: {
    isLogged() {
      return getStore().state.user.logged;
    },
  },
  watch: {
    "event.subscribers": {
      handler: async function (newSubscribers) {
        if (this.isLogged) {
          const account = await UserRepository.findOneBase(
            getStore().state.user.id
          );
          const index = newSubscribers.findIndex(
            (subscriber) => subscriber.id === account.id
          );
          this.isSubscribed = index >= 0;
        }
      },
      immediate: true,
      deep: true,
    },
    "event.saves": {
      handler: async function (newSaves) {
        if (this.isLogged) {
          const account = await UserRepository.findOneBase(
            getStore().state.user.id
          );
          const index = newSaves.findIndex((save) => save.id === account.id);
          this.isSaved = index >= 0;
        }
      },
      immediate: true,
      deep: true,
    },
  },
  mounted() {
    this.coordinatesToLocation();
  },
};
</script>

<style scoped></style>
