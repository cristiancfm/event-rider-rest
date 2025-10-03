<template>
  <EventDetails
    v-if="event"
    :event="event"
    @subscribers="updateSubscribers"
    @saves="updateSaves"
  ></EventDetails>
</template>

<script>
import EventRepository from "@/repositories/EventRepository";
import EventDetails from "@/components/events/EventDetails.vue";
import { updateSubscribers, updateSaves } from "@/common/event";

export default {
  name: "EventDetailsView",
  data() {
    return {
      event: null,
    };
  },
  components: { EventDetails },
  mounted() {
    this.fetchData();
  },
  methods: {
    async fetchData() {
      try {
        this.event = await EventRepository.findOne(this.$route.params.id);
      } catch (err) {
        console.error(err);
      }
    },
    updateSubscribers,
    updateSaves,
  },
};
</script>

<style scoped></style>
