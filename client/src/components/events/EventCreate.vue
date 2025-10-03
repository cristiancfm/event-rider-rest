<template>
  <div class="m-auto" style="max-width: 720px">
    <div class="text-start p-2">
      <h2 class="m-2">
        Create <span style="font-family: 'Arial Black', serif">Event</span>
      </h2>
      <form @submit.prevent="createEvent()">
        <div class="row p-2">
          <div class="col-6">
            <div class="mb-2">
              <label for="title" class="form-label">Title</label>
              <input
                type="text"
                class="form-control"
                id="title"
                v-model="eventForm.title"
                required
              />
            </div>
            <div class="mb-2">
              <label for="startingDate" class="form-label">
                <i class="bi bi-calendar-week-fill"></i>
                Starting Date & Time
              </label>
              <input
                type="datetime-local"
                class="form-control"
                id="startingDate"
                v-model="eventForm.startingDate"
                required
              />
            </div>
            <div class="mb-2">
              <label for="endingDate" class="form-label">
                <i class="bi bi-calendar-week-fill"></i>
                Ending Date & Time
              </label>
              <input
                type="datetime-local"
                class="form-control"
                id="endingDate"
                v-model="eventForm.endingDate"
                required
              />
            </div>
            <div class="mb-2">
              <label for="category" class="form-label">
                <i class="bi bi-ticket-perforated-fill"></i>
                Category
              </label>
              <div class="row">
                <div class="col">
                  <div class="form-check">
                    <input
                      type="radio"
                      class="form-check-input me-1"
                      id="existingCategory"
                      name="categoryType"
                      v-model="eventForm.existingCategoryChecked"
                      :value="true"
                    />
                    <label for="existingCategory" class="form-check-label">
                      Existing category
                    </label>
                  </div>
                  <select
                    class="form-control form-select"
                    id="category"
                    v-model="eventForm.existingCategoryId"
                    :disabled="!eventForm.existingCategoryChecked"
                    required
                  >
                    <option
                      v-for="eventCategory in eventCategories"
                      :key="eventCategory.id"
                      :value="eventCategory.id"
                    >
                      {{ eventCategory.name }}
                    </option>
                  </select>
                </div>
                <!--                <div class="col-6">-->
                <!--                  <div class="form-check">-->
                <!--                    <input-->
                <!--                      type="radio"-->
                <!--                      class="form-check-input me-1"-->
                <!--                      id="newCategory"-->
                <!--                      name="categoryType"-->
                <!--                      v-model="eventForm.existingCategoryChecked"-->
                <!--                      :value="false"-->
                <!--                    />-->
                <!--                    <label for="newCategory" class="form-check-label">-->
                <!--                      New category-->
                <!--                    </label>-->
                <!--                  </div>-->
                <!--                  <input-->
                <!--                    type="text"-->
                <!--                    class="form-control"-->
                <!--                    id="categoryNew"-->
                <!--                    v-model="eventForm.newCategory"-->
                <!--                    :disabled="eventForm.existingCategoryChecked"-->
                <!--                    required-->
                <!--                  />-->
                <!--                </div>-->
              </div>
              <!-- Info card -->
              <!--              <div class="row p-1 m-1 mt-2 bg-info" style="border-radius: 5px">-->
              <!--                <p class="mt-1">-->
              <!--                  <i class="bi bi-info-circle-fill"></i> New categories will be-->
              <!--                  reviewed by administrators before they become available-->
              <!--                </p>-->
              <!--              </div>-->
              <!-- **** -->
            </div>
            <div class="mb-2">
              <label for="location" class="form-label">
                <i class="bi bi-geo-alt-fill"></i>
                Location
              </label>
              <div class="input-group">
                <span class="input-group-text">
                  <i class="bi bi-search"></i>
                </span>
                <input
                  type="search"
                  class="form-control"
                  id="location"
                  v-model="locationInput"
                  list="locations-list"
                  @input="autocompleteLocation"
                  required
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
            <div class="mb-2">
              <label for="locationDetails" class="form-label">
                Location Details (floor, etc.) (optional)
              </label>
              <input
                type="text"
                class="form-control"
                id="locationDetails"
                v-model="eventForm.locationDetails"
              />
            </div>
            <div class="mb-2">
              <label for="description" class="form-label"> Description </label>
              <textarea
                class="form-control"
                id="description"
                v-model="eventForm.description"
                rows="4"
                required
              />
            </div>
            <button
              type="submit"
              class="btn btn-primary mt-2"
              @click="eventForm.error = null"
            >
              Create Event
            </button>
            <button
              class="btn btn-secondary mt-2 ms-2"
              @click.prevent="$router.go(-1)"
            >
              Cancel
            </button>
            <!-- Info card -->
            <div
              v-if="!(isAdmin || isVerifiedUser)"
              class="row p-1 m-1 mt-3 bg-info"
              style="border-radius: 5px"
            >
              <p class="mt-1">
                <i class="bi bi-info-circle-fill"></i> Since you are an
                unverified user, this event will be reviewed by administrators
                before it becomes available
              </p>
            </div>
            <!-- **** -->
          </div>
          <div class="col-6">
            <image-selector
              ref="imageSelector"
              :maxFiles="10"
              :maxFileSize="5 * 1024 * 1024"
            />
            <div
              class="mt-2"
              v-if="
                eventForm.title === '' ||
                eventForm.coordinateX === '' ||
                eventForm.coordinateY === ''
              "
            >
              <div style="aspect-ratio: 2/3">
                <img
                  src="/map-placeholder.png"
                  class="d-block w-100"
                  style="object-fit: cover; width: 100%; height: 100%"
                  alt="Map placeholder"
                />
              </div>
            </div>
            <div v-else>
              <EventMap
                :events="[eventForm]"
                @popup-closed="popupClosed"
              ></EventMap>
            </div>
            <div
              class="alert alert-danger alert-dismissible fade show mt-3"
              role="alert"
              v-if="eventForm.error"
            >
              <strong>Error:</strong> {{ eventForm.error }}
              <button
                type="button"
                class="btn-close"
                data-bs-dismiss="alert"
                aria-label="Close"
                @click="eventForm.error = null"
              ></button>
            </div>
          </div>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import { MAPBOX_TOKEN } from "@/constants";
import { getStore } from "@/common/store";
import { MapBoxProvider } from "leaflet-geosearch";
import EventMap from "@/components/events/EventMap.vue";
import EventCategoriesRepository from "@/repositories/EventCategoryRepository";
import EventRepository from "@/repositories/EventRepository";
import ImageSelector from "@/components/ImageSelector";

export default {
  name: "EventCreate",
  data() {
    return {
      locationInput: "",
      locationList: [],
      eventCategories: [],
      eventForm: {
        title: "",
        startingDate: "",
        endingDate: "",
        existingCategoryChecked: true,
        existingCategoryId: null,
        newCategory: null,
        coordinateX: "",
        coordinateY: "",
        locationDetails: "",
        description: "",
        error: null,
      },
      newCreatedEvent: {
        id: null,
      },
    };
  },
  components: { ImageSelector, EventMap },
  methods: {
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
        // - coordinateX is the latitude
        // - coordinateY is the longitude
        this.eventForm.coordinateX = results[0].y;
        this.eventForm.coordinateY = results[0].x;
        console.log(this.eventForm.startingDate);
      } else {
        this.eventForm.coordinateX = "";
        this.eventForm.coordinateY = "";
      }
    },
    showEventInMap(event) {
      this.showInMapEvent = event;
    },
    popupClosed() {
      this.showInMapEvent = null;
    },
    async createEvent() {
      try {
        await EventRepository.save(this.eventForm).then((response) => {
          this.newCreatedEvent.id = response.id;
        });
        if (this.$refs.imageSelector.imagesToUpload.length > 0) {
          for (const image of this.$refs.imageSelector.imagesToUpload) {
            await EventRepository.saveEventImage(
              this.newCreatedEvent.id,
              image.file
            );
          }
        }
        this.$router.go(-1);
      } catch (err) {
        const response = JSON.parse(err.request.response);
        this.eventForm.error = response.message;
        console.error(err);
      }
    },
  },
  computed: {
    isLogged() {
      return getStore().state.user.logged;
    },
    isAdmin() {
      return getStore().state.user.authority === "ADMIN";
    },
    isVerifiedUser() {
      return getStore().state.user.authority === "USER_VERIFIED";
    },
  },
  mounted() {
    EventCategoriesRepository.findAll().then((response) => {
      this.eventCategories = response;
    });
  },
};
</script>

<style scoped></style>
