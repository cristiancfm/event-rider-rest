<template>
  <div class="m-2">
    <p>
      <button
        class="btn btn-secondary btn-collapse"
        type="button"
        data-bs-toggle="collapse"
        data-bs-target="#collapseFilters"
        aria-expanded="false"
        aria-controls="collapseFilters"
      >
        Show Filters <i class="bi bi-caret-down-fill"></i>
      </button>
    </p>
    <form class="collapse" id="collapseFilters" @submit.prevent="emitFilters">
      <fieldset
        class="reset-style bg-gradient bg-light"
        style="border-radius: 10px"
      >
        <legend class="reset-style ps-2 pe-2">Filters</legend>
        <div class="d-flex flex-wrap">
          <div class="col me-3" style="min-width: 150px">
            <label for="event-title" class="form-label m-0 mt-1">
              Event Title
            </label>
            <input
              type="search"
              id="event-title"
              class="form-control form-control-sm"
              v-model="filters.title"
            />
          </div>
          <div class="col me-3" style="min-width: 150px">
            <label for="event-location" class="form-label m-0 mt-1">
              Location
            </label>
            <div class="input-group input-group-sm">
              <span class="input-group-text">
                <i class="bi bi-search"></i>
              </span>
              <input
                type="search"
                id="event-location"
                class="form-control form-control-sm"
                v-model="locationInput"
                list="locations-list"
                @input="autocompleteLocation"
              />
              <datalist id="locations-list" v-if="locationList.length > 0">
                <option selected value="">(all)</option>
                <option
                  v-for="location in locationList"
                  :key="location"
                  :value="location.label"
                >
                  {{ location.label }}
                </option>
              </datalist>
            </div>
          </div>
          <div class="col me-3" style="min-width: 150px">
            <label for="event-distance" class="form-label m-0 mt-1">
              Distance
            </label>
            <select
              id="event-distance"
              class="form-control form-control-sm form-select form-select-sm"
              v-model="filters.distance"
              :disabled="this.locationInput === ''"
            >
              <option selected value="">(all)</option>
              <option value="5000">&lt; 5 km</option>
              <option value="10000">5 - 10 km</option>
              <option value="50000">10 - 50 km</option>
              <option value="100000">50 - 100 km</option>
              <option value="500000">100 - 500 km</option>
            </select>
          </div>
          <div class="col me-3" style="min-width: 150px">
            <label for="event-date" class="form-label m-0 mt-1">Date</label>
            <input
              type="date"
              id="event-date"
              class="form-control form-control-sm"
              v-model="filters.date"
            />
          </div>
          <div class="col me-3" style="min-width: 150px">
            <label for="event-category" class="form-label m-0 mt-1">
              Category
            </label>
            <select
              id="event-category"
              class="form-control form-control-sm form-select form-select-sm"
              v-model="filters.category"
            >
              <option selected value="">(all)</option>
              <option
                v-for="eventCategory in eventCategories"
                :key="eventCategory.id"
                :value="eventCategory.id"
              >
                {{ eventCategory.name }}
              </option>
            </select>
          </div>
          <div class="col-12 col-sm me-3" style="min-width: 150px">
            <div class="form-check mt-4">
              <input
                type="checkbox"
                id="cancelled-events"
                class="form-check-input"
                v-model="filters.cancelled"
              />
              <label class="form-check-label" for="cancelled-events">
                Cancelled events
              </label>
            </div>
          </div>
          <div class="col-auto pt-4 me-3">
            <button class="btn btn-primary mt-1" type="submit">Apply</button>
          </div>
          <div class="col-auto pt-4 me-3">
            <button
              class="btn btn-secondary mt-1"
              type="reset"
              @click="clearFilters"
            >
              Clear
            </button>
          </div>
        </div>
      </fieldset>
    </form>
  </div>
</template>

<script>
import EventCategoriesRepository from "@/repositories/EventCategoryRepository";
import { MapBoxProvider } from "leaflet-geosearch";
import { MAPBOX_TOKEN } from "@/constants";

export default {
  name: "EventFilters",
  data() {
    return {
      locationInput: "",
      locationList: [],
      filters: {
        title: "",
        latitude: "",
        longitude: "",
        date: "",
        distance: "",
        category: "",
        cancelled: false,
      },
      eventCategories: [],
    };
  },
  methods: {
    emitFilters() {
      this.$emit("filters-applied", this.filters);
    },
    async autocompleteLocation() {
      if (this.locationInput !== "") {
        const provider = new MapBoxProvider({
          params: {
            access_token: MAPBOX_TOKEN,
            proximity: "ip", //the places closer to the user IP are shown first
          },
        });
        const results = await provider.search({ query: this.locationInput });
        this.locationList = results;
        //take latitude and longitude from first result
        this.filters.latitude = results[0].y;
        this.filters.longitude = results[0].x;
      } else {
        this.filters.latitude = "";
        this.filters.longitude = "";
      }
    },
    clearFilters() {
      this.locationInput = "";
      this.filters.title = "";
      this.filters.latitude = "";
      this.filters.longitude = "";
      this.filters.date = "";
      this.filters.distance = "";
      this.filters.category = "";
      this.emitFilters();
    },
  },
  mounted() {
    EventCategoriesRepository.findAll().then((response) => {
      this.eventCategories = response;
    });
    // Set category if the URL is /event-categories/:id
    if (this.$route.path.startsWith("/event-categories/")) {
      if (this.$route.params.id) {
        this.filters.category = this.$route.params.id;
      }
    }
  },
};
</script>

<style scoped>
.reset-style {
  all: revert;
}
@media (min-width: 768px) {
  /* Styles for big screens */
  #collapseFilters {
    display: block; /* Show filters on big screens */
  }
  .btn-collapse {
    display: none; /* Hide button on big screens */
  }
}
</style>
